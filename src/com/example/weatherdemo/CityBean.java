package com.example.weatherdemo;

public class CityBean {

	public String code;
	public String name;

	public String getInfo() {
		return code + ":" + name;
	}

	public void setData(String p) {
		String[] ps = p.split(":");
		code = ps[0];
		name = ps[1];
	}

}
