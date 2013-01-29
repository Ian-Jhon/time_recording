package com.yvelabs.timerecording.utils;

public class MyKeyValuePair {
	
	private Object key;
	private Object value;
	
	@Override
	public String toString() {
		return value == null ? null : value.toString();
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
