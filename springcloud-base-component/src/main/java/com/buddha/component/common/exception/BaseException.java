package com.buddha.component.common.exception;

import com.buddha.component.common.enums.ResultStatusEnum;


/**
 * 
 * 异常基础类
 */
@SuppressWarnings("serial")
public class BaseException extends RuntimeException {

	/**
	 * 返回错误码
	 */
	private Integer code;

	/**
	 * 返回信息
	 */
	private String msg;
	
	/**
	 * 构造 （默认返回码：601）
	 * 
	 * @param msg
	 *            提示信息
	 */
	public BaseException(String msg) {
		code = ResultStatusEnum.COMMON_FAIL.getCode();
		this.msg = msg;
	}

	/**
	 * 构造
	 * 
	 * @param enumCode
	 * @param msg
	 */
	public BaseException(ResultStatusEnum enumCode) {
		super(enumCode.getMsg());
		this.code = enumCode.getCode();
		this.msg = enumCode.getMsg();
	}

	/**
	 * 构造
	 * 
	 * @param enumCode
	 * @param msg
	 */
	public BaseException(ResultStatusEnum enumCode, String msg) {
		super(msg);
		this.msg = msg;
		this.code = enumCode.getCode();
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
