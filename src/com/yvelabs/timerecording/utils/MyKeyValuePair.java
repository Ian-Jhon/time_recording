package com.yvelabs.timerecording.utils;

public class MyKeyValuePair {
	
	private Object key;
	private String value;
	
	public MyKeyValuePair() {
		
	}
	
	public MyKeyValuePair (Object key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
