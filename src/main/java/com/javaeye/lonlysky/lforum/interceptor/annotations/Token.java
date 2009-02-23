package com.javaeye.lonlysky.lforum.interceptor.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>防止表单重复提交注解</b><br>
 * 注：方法注解,@Token
 * 
 * @author 黄磊
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Token {

}