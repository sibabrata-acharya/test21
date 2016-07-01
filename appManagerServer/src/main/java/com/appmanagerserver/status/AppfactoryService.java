package com.appmanagerserver.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.appmanagerserver.blueprints.AppfactoryResponse;
import com.appmanagerserver.exception.CustomErrorMessage;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.utils.ExceptionUtils;
import com.appmanagerservice.sendinterface.ICacheHandler;


public class AppfactoryService {
	@Autowired
	private ExceptionUtils eUtils;	
	private Map<String, ICacheHandler> cacheInstanceMap = new HashMap<String, ICacheHandler>();
	public AppfactoryResponse deployStatus(String bluePrintID) throws MyException{
		try {
			String cacheSelect = "";
			ICacheHandler cacheHandler = cacheInstanceMap.get(cacheSelect);
			return (AppfactoryResponse) cacheHandler.getCacheData(bluePrintID);
		} catch (Exception e) {
			throw eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, e.getMessage());
		}

	}
}
