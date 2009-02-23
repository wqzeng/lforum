package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 积分操作类型，如发表主题等
 * 
 * @author 黄磊
 *
 */
public interface CreditsOperationType {

	/**
	 * 作者发新主题增加的积分数, 如果该主题被删除, 作者积分也会按此标准相应减少
	 */
	public static final int POSTTOPIC = 4;

	/**
	 * 作者发新回复增加的积分数, 如果该回复被删除, 作者积分也会按此标准相应减少
	 */
	public static final int POSTREPLY = 5;

	/**
	 * 主题被加入精华时单位级别作者增加的积分数(根据精华级别乘以1～3), 如果该主题被移除精华, 作者积分也会按此标准相应减少
	 */
	public static final int DIGEST = 6;

	/**
	 * 用户每上传一个附件增加的积分数, 如果该附件被删除, 发布者积分也会按此标准相应减少
	 */
	public static final int UPLOADATTACHMENT = 7;

	/**
	 * 用户每下载一个附件扣除的积分数. 注意: 如果允许游客组下载附件, 本策略将可能被绕过
	 */
	public static final int DOWNLOADATTACHMENT = 8;

	/**
	 * 用户每发送一条短消息扣除的积分数
	 */
	public static final int SENDMESSAGE = 9;

	/**
	 * 用户每进行一次帖子搜索或短消息搜索扣除的积分数
	 */
	public static final int SEARCH = 10;

	/**
	 * 用户每成功进行一次交易后增加的积分数
	 */
	public static final int TRADESUCCEED = 11;

	/**
	 * 用户每参与一次投票后增加的积分数
	 */
	public static final int VOTE = 12;
}
