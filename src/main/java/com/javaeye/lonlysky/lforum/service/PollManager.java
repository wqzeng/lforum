package com.javaeye.lonlysky.lforum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.hibernate.SimpleHibernateTemplate;

import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.entity.forum.PollOptionView;
import com.javaeye.lonlysky.lforum.entity.forum.Polloptions;
import com.javaeye.lonlysky.lforum.entity.forum.Polls;
import com.javaeye.lonlysky.lforum.entity.forum.Topics;
import com.javaeye.lonlysky.lforum.entity.forum.Users;

/**
 * 投票操作类
 * 
 * @author 黄磊
 *
 */
@Service
@Transactional
public class PollManager {

	private SimpleHibernateTemplate<Polls, Integer> pollDAO;
	private SimpleHibernateTemplate<Polloptions, Integer> polloptionDAO;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		pollDAO = new SimpleHibernateTemplate<Polls, Integer>(sessionFactory, Polls.class);
		polloptionDAO = new SimpleHibernateTemplate<Polloptions, Integer>(sessionFactory, Polloptions.class);
	}

	/**
	 * 创建一个投票
	 * @param topic 关联的主题 	
	 * @param multiple 投票类型, 0为单选, 1为多选
	 * @param itemcount 投票项总数 	
	 * @param itemnamelist 用户名列表
	 * @param itemvaluelist 投票项目结果列表
	 * @param enddatetime 截止日期
	 * @param user 用户
	 * @param maxchoices 最多可选项数
	 * @param visible 提交投票后结果才可见, 0为可见, 1为投票后可见
	 * @return 成功则返回true, 否则返回false
	 */
	public boolean createPoll(Topics topic, int multiple, int itemcount, String itemnamelist, String itemvaluelist,
			String enddatetime, Users user, int maxchoices, int visible) {
		String[] itemname = itemnamelist.split("\r\n");
		String[] itemvalue = itemvaluelist.split("\r\n");
		if ((itemname.length != itemcount) || (itemvalue.length != itemcount)) {
			return false;
		}

		Polls pollinfo = new Polls();
		pollinfo.setDisplayorder(0);
		pollinfo.setExpiration(enddatetime);
		pollinfo.setMaxchoices(maxchoices);
		pollinfo.setMultiple(multiple);
		pollinfo.setTopics(topic);
		pollinfo.setUsers(user);
		pollinfo.setVisible(visible);
		pollinfo.setVoternames("");

		pollDAO.save(pollinfo);
		topic.setPoll(1);
		topicManager.updateTopicInfo(topic);

		if (pollinfo.getPollid() > 0) {
			for (int i = 0; i < itemcount; i++) {
				Polloptions polloptioninfo = new Polloptions();
				polloptioninfo.setDisplayorder(i + 1);
				polloptioninfo.setPolls(pollinfo);
				polloptioninfo.setPolloption(Utils.format(itemname[i], 80, false));
				polloptioninfo.setTopics(topic);
				polloptioninfo.setVoternames("");
				polloptioninfo.setVotes(0);
				polloptionDAO.save(polloptioninfo);
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean updatePoll(Topics topics, int multiple, int itemcount, String polloptionidlist, String itemnamelist,
			String itemdisplayorderlist, String enddatetime, int maxchoices, int visible) {
		//
		String[] itemname = itemnamelist.split("\r\n");
		String[] itemdisplayorder = itemdisplayorderlist.split("\r\n");
		String[] polloptionid = polloptionidlist.split("\r\n");

		if ((itemname.length != itemcount) || (itemdisplayorder.length != itemcount)) {
			return false;
		}

		Polls pollinfo = getPollInfo(topics.getTid());
		pollinfo.setExpiration(enddatetime);
		pollinfo.setMaxchoices(maxchoices);
		pollinfo.setMultiple(multiple);
		pollinfo.setTopics(topics);
		pollinfo.setVisible(visible);

		boolean result = false;
		if (pollinfo.getPollid() > 0) {
			pollDAO.save(pollinfo);
			result = true;
		}
		if (result) {
			List<Polloptions> polloptioninfocoll = getPollOptionInfoCollection(pollinfo.getTopics().getTid());
			int i = 0;

			//先作已存在的投票选项更新及新添加选项的添加操作
			boolean optionexist;
			for (String optionid : polloptionid) {
				optionexist = false;
				for (Polloptions polloptioninfo : polloptioninfocoll) {
					if (optionid.equals(polloptioninfo.getPolloptionid().toString())) {
						polloptioninfo.setPolls(pollinfo);
						polloptioninfo.setPolloption(Utils.format(itemname[i], 80, false));
						polloptioninfo.setDisplayorder(Utils.null2String(itemdisplayorder[i]).equals("") ? i + 1
								: Utils.null2Int(itemdisplayorder[i], 0));
						polloptionDAO.save(polloptioninfo);
						optionexist = true;
						break;
					}
				}
				if (!optionexist) //如果当前选项不存在，表示该选项为新添选项
				{
					Polloptions polloptioninfo = new Polloptions();
					polloptioninfo.setDisplayorder(Utils.null2String(itemdisplayorder[i]).equals("") ? i + 1 : Utils
							.null2Int(itemdisplayorder[i], 0));
					polloptioninfo.setPolls(pollinfo);
					polloptioninfo.setPolloption(Utils.format(itemname[i], 80, false));
					polloptioninfo.setTopics(topics);
					polloptioninfo.setVoternames("");
					polloptioninfo.setVotes(0);
					polloptionDAO.save(polloptioninfo);
				}
				i++;
			}

			for (Polloptions polloptioninfo : polloptioninfocoll) {
				//下面代码用于删除已去除的投票项
				if (("\r\n" + polloptionidlist + "\r\n").indexOf("\r\n" + polloptioninfo.getPolloptionid() + "\r\n") < 0) {
					polloptionDAO.delete(polloptioninfo);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过主题ID获取相应的投票信息
	 * @param tid 投票主题的id
	 * @return 投票信息
	 */
	public Polls getPollInfo(int tid) {
		List<Polls> polls = pollDAO.findByProperty("topics.tid", tid);
		return polls.size() > 0 ? polls.get(0) : new Polls();
	}

	/**
	 * 通过主题ID获取相应的投票信息
	 * @param tid 主题id
	 * @return 投票选项集合
	 */
	public List<Polloptions> getPollOptionInfoCollection(int tid) {
		return polloptionDAO.findByProperty("topics.tid", tid);
	}

	/**
	 * 获得与指定主题id关联的投票数据
	 * @param tid 主题id
	 * @return
	 */
	public List<PollOptionView> getPollOptionList(int tid) {
		List<PollOptionView> opList = new ArrayList<PollOptionView>();
		List<Polloptions> polloptionList = getPollOptionInfoCollection(tid);

		int multiple = getPollInfo(tid).getMultiple();

		int votesum = 0;
		int maxVote = 0;
		for (Polloptions polloptions : polloptionList) {
			votesum += polloptions.getVotes();
			maxVote = polloptions.getVotes() > maxVote ? polloptions.getVotes() : maxVote;
		}

		if (votesum == 0) {
			votesum = 1;
		}
		int i = 0;
		String votername = "";

		//组定数据项
		for (Polloptions polloptions : polloptionList) {
			PollOptionView opView = new PollOptionView();
			opView.setName(polloptions.getPolloption());
			opView.setValue(polloptions.getVotes());
			opView.setBarid(i % 10);
			opView.setBarwidth(Utils.round(
					(((double) (Utils.null2Double(polloptions.getVotes(), 0) * 100 / votesum) / 100) * 200 + 3), 2));
			opView.setPercent(Utils.round(
					(double) (Utils.null2Double(polloptions.getVotes(), 0) * 100 / votesum) / 100, 2));
			opView.setMultiple(multiple);
			opView.setPolloptionid(polloptions.getPolloptionid());
			opView.setDisplayorder(polloptions.getDisplayorder());

			if (!Utils.null2String(polloptions.getVoternames()).equals("")) {
				//将投票人字段的数据重新组合成链接字符串
				for (String username : polloptions.getVoternames().trim().split("\r\n")) {
					votername += "<a href=\"userinfo.action?username=" + Utils.urlEncode(username.trim()) + "\">"
							+ username.trim() + "</a> ";
				}
				opView.setVotername(votername);
				votername = "";
			} else {
				opView.setVotername("");
			}
			opView.setPercentwidth((420 * ((double) (Utils.null2Double(polloptions.getVotes(), 0) / maxVote))));
			opList.add(opView);
			i++;
		}
		return opList;
	}

	/**
	 * 获取参与投票者名单
	 * @param tid 投票的主题id
	 * @param userid 当前投票用户id
	 * @param username 当前投票用户id
	 * @param allowvote 是否允许投票
	 * @return 参与投票者名单
	 */
	public String getVoters(int tid, int userid, String username, Map<Integer, Boolean> allowvote) {
		String strUsernamelist = Utils.null2String(pollDAO.findUnique(
				"select voternames from Polls where topics.tid=?", tid));
		// 如果已投票用户uid列表为"" (尚无人投票), 则立即返回true
		allowvote.put(0, true);
		if (strUsernamelist.equals("")) {
			return "";
		}
		String[] votername = strUsernamelist.trim().split("\r\n");
		StringBuilder sbVoters = new StringBuilder();
		for (int i = 0; i < votername.length; i++) {
			if (votername[i].trim().equals(username)) {
				allowvote.put(0, false);
			}

			if (userid == -1 && Utils.inArray(tid + "", ForumUtils.getCookie("polled"))) {
				allowvote.put(0, false);
			}

			if (votername[i].indexOf(' ') == -1) {
				sbVoters.append("<a href=\"userinfo.action?username=");
				sbVoters.append(Utils.urlEncode(votername[i].trim()));
				sbVoters.append("\">");
				sbVoters.append(votername[i]);
				sbVoters.append("</a>");
			} else {
				sbVoters.append(votername[i].substring(0, votername[i].lastIndexOf(".") + 1).trim().replace(" ", "")
						+ "]");
			}
			sbVoters.append("&nbsp; ");
		}

		return sbVoters.toString();
	}

	/**
	 * 得到投票帖的结束时间
	 * @param tid 主题id
	 * @return 结束时间
	 */
	public String getPollEnddatetime(int tid) {
		return Utils.null2String(pollDAO.findUnique("select expiration from Polls where topics.tid=?", tid));
	}

	/**
	 * 判断是否允许指定用户投票
	 * @param tid 主题id
	 * @param username 用户名
	 * @return 判断结果
	 */
	public boolean allowVote(int tid, String username) {
		if (username == "") {
			return false;
		}
		String strUsernamelist = getPollUserNameList(tid);

		// 如果已投票用户uid列表为"" (尚无人投票), 则立即返回true
		if (strUsernamelist.equals("")) {
			return true;
		}

		// 检查已投票用户uid列表中是否存在指定用户的uid
		String[] usernamelist = strUsernamelist.trim().split("\r\n");
		int itemcount = usernamelist.length;
		for (int i = 0; i < itemcount; i++) {
			if (usernamelist[i].trim().equals(username)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得投票的用户名
	 * @param tid 主题Id
	 * @return
	 */
	public String getPollUserNameList(int tid) {
		return Utils.null2String(pollDAO.findUnique("select voternames from Polls where topics.tid=?", tid));
	}

	/**
	 * 根据投票信息更新数据库中的记录
	 * @param tid 主题id
	 * @param selitemidlist 选择的投票项id列表
	 * @param username 用户名
	 * @return 如果执行成功则返回0, 非法提交返回负值
	 */
	public int updatePoll(int tid, String selitemidlist, String username) {
		if (username.equals("")) {
			return -1;
		}

		String[] selitemid = selitemidlist.split(",");
		int nowid = 0;
		for (String optionid : selitemid) {
			nowid = Utils.null2Int(optionid, -1);
			// 如果id结果集合中出现非数字类型字符则认为是非法
			if (nowid == -1) {
				return -1;
			}
		}

		Polls pollinfo = getPollInfo(tid);

		if (pollinfo.getPollid() < 1) {
			return -3;
		}

		if (pollinfo.getVoternames().trim().equals("")) {
			pollinfo.setVoternames(username);
		} else {
			pollinfo.setVoternames(pollinfo.getVoternames() + "\r\n" + username);
		}

		pollDAO.save(pollinfo);
		List<Polloptions> polloptioninfocoll = getPollOptionInfoCollection(pollinfo.getTopics().getTid());

		for (String optionid : selitemid) {
			for (Polloptions polloptioninfo : polloptioninfocoll) {
				if (Utils.null2Int(optionid) == polloptioninfo.getPolloptionid()) {
					if (polloptioninfo.getVoternames().trim().equals("")) {
						polloptioninfo.setVoternames(username);
					} else {
						polloptioninfo.setVoternames(pollinfo.getVoternames() + "\r\n" + username);
					}
					polloptioninfo.setVotes(polloptioninfo.getVotes() + 1);
					polloptionDAO.save(polloptioninfo);
				}
			}
		}

		return 0;
	}
}
