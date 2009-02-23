package com.javaeye.lonlysky.lforum.exception;

/**
 * 业务层异常
 * 
 * @author 黄磊
 * 
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 6272757536995044660L;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable t) {
		super(t);
		setStackTrace(t.getStackTrace());
	}

	public ServiceException(String msg, Throwable t) {
		super(msg, t);
		setStackTrace(t.getStackTrace());
	}
}
