package com.appfactory.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PaasCatalog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	} 

}
