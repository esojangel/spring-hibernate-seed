package com.jay.util;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Jay Zhang
 *
 */
@SuppressWarnings("serial")
public class AppException extends RuntimeException {
	private String code ="";
	private List<Object> args = null;
	
	public AppException(String code) {
		super(code);
		this.code = code;
    }
	
	public AppException(String code,Object... args) {
		super(code);
		this.code = code;
		this.args = Arrays.asList(args);
    }
	
	public AppException(Throwable cause, String code,Object... args) {
		super(code, cause);
		this.code = code;
		this.args = Arrays.asList(args);
    }
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	@Override
	public String toString() {
		StringBuilder sb =new StringBuilder();
		if(args!=null){
			for(Object arg : args) {
				sb.append("\"");
				sb.append(arg);
				sb.append("\",");
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return "{\"code\":\"" + code + "\", \"args\":[" + sb.toString() + "]}";
	}
}
