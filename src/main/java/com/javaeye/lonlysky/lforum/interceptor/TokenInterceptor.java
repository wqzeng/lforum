package com.javaeye.lonlysky.lforum.interceptor;

import java.lang.reflect.Method;

import org.apache.struts2.ServletActionContext;

import com.javaeye.lonlysky.lforum.BaseAction;
import com.javaeye.lonlysky.lforum.GlobalsKeys;
import com.javaeye.lonlysky.lforum.RequestConfig;
import com.javaeye.lonlysky.lforum.interceptor.annotations.Token;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 扩展默认防止表单重复提交拦截器<br>
 * 注：Action方法注解@Token即可
 * 
 * @author 黄磊
 * 
 */
public class TokenInterceptor extends org.apache.struts2.interceptor.TokenInterceptor {

	private static final long serialVersionUID = 3770168408651787077L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		Object action = invocation.getAction();
		if (action != null) {
			Method method = getActionMethod(action.getClass(), invocation.getProxy().getMethod());
			Token token = method.getAnnotation(Token.class);
			if (token != null) {
				String result = super.doIntercept(invocation);
				if (INVALID_TOKEN_CODE.equals(result)) {
					if (action instanceof BaseAction) {
						RequestConfig reqcfg = ((BaseAction) action).getReqcfg().addErrLine("非法访问该连接").addErrLine(
								"重复的提交表单");
						ServletActionContext.getRequest().setAttribute(GlobalsKeys.REQUEST_CONFIG, reqcfg);
						return GlobalsKeys.WEB_MESSAGE;
					}
				}
				return result;
			}
		}
		return invocation.invoke();
	}

	public static final Method getActionMethod(Class<? extends Object> actionClass, String methodName)
			throws NoSuchMethodException {
		Method method;
		try {
			method = actionClass.getMethod(methodName, new Class[0]);
		} catch (NoSuchMethodException e) {
			String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
			method = actionClass.getMethod(altMethodName, new Class[0]);
		}
		return method;
	}

}
