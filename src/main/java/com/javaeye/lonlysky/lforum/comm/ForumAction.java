package com.javaeye.lonlysky.lforum.comm;

/**
 * 定义论坛动作
 * 
 * @author 黄磊
 *
 */
public final class ForumAction {

	/**
	 * 根据动作ID返回指定的动作名称
	 * @param actionid 动作ID
	 * @return 动作名称
	 */
	public static String getActionDescriptionByID(int actionid) {
		switch (actionid) {
		case IndexShow.ACTION_ID:
			return IndexShow.ACTION_DESCRIPTION;
		case ShowForum.ACTION_ID:
			return ShowForum.ACTION_DESCRIPTION;
		case ShowTopic.ACTION_ID:
			return ShowTopic.ACTION_DESCRIPTION;
		case Login.ACTION_ID:
			return Login.ACTION_DESCRIPTION;
		case PostTopic.ACTION_ID:
			return PostTopic.ACTION_DESCRIPTION;
		case PostReply.ACTION_ID:
			return PostReply.ACTION_DESCRIPTION;
		case ActivationUser.ACTION_ID:
			return ActivationUser.ACTION_DESCRIPTION;
		case Register.ACTION_ID:
			return Register.ACTION_DESCRIPTION;
		default:
			return "";
		}
	}

	/**
	 * 首页动作定义
	 */
	public final class IndexShow {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 1;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "IndexShow";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "浏览首页";
	}

	/**
	 * 板块动作定义
	 */
	public final class ShowForum {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 2;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "ShowForum";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "浏览论坛板块";
	}

	/**
	 * 主题显示动作定义
	 */
	public final class ShowTopic {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 3;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "ShowTopic";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "浏览帖子";
	}

	/**
	 * 用户登陆动作定义
	 */
	public final class Login {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 4;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "Login";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "论坛登陆";
	}

	/**
	 * 发表主题动作定义
	 */
	public final class PostTopic {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 5;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "PostTopic";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "发表主题";
	}

	/**
	 * 发表回复动作定义
	 */
	public final class PostReply {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 6;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "PostReply";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "发表回复";
	}

	/**
	 * 激活用户动作定义
	 */
	public final class ActivationUser {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 7;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "ActivationUser";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "激活用户帐户";
	}

	/**
	 * 注册动作定义
	 */
	public final class Register {
		/**
		 * 动作ID
		 */
		public static final int ACTION_ID = 8;

		/**
		 * 动作名称
		 */
		public static final String ACTION_NAME = "Register";

		/**
		 * 动作描述
		 */
		public static final String ACTION_DESCRIPTION = "注册新用户";
	}

}
