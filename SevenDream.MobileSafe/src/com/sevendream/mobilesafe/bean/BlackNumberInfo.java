package com.sevendream.mobilesafe.bean;

public class BlackNumberInfo {

	private String number;//黑名单电话号码
	private String mode;//拦截模式
	
	public String getNumber() {
		return number; 
	}
	public String getMode() {
		return mode; 
	}
	public void setNumber(String number) {
		this.number = number; 
	}
	public void setMode(String mode) {
		this.mode = mode; 
	}
	
}
