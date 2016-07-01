package com.appmanagerserver.blueprints;

import java.io.Serializable;

public class AppfactoryResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7455516059573038531L;
	private String finalstatus;
	private String cfAppURL;
	private String localDownloadPath;
	public String getFinalstatus() {
		return finalstatus;
	}
	public void setFinalstatus(String finalstatus) {
		this.finalstatus = finalstatus;
	}
	public String getCfAppURL() {
		return cfAppURL;
	}
	public void setCfAppURL(String cfAppURL) {
		this.cfAppURL = cfAppURL;
	}
	public String getLocalDownloadPath() {
		return localDownloadPath;
	}
	public void setLocalDownloadPath(String localDownloadPath) {
		this.localDownloadPath = localDownloadPath;
	}
	
	
}
