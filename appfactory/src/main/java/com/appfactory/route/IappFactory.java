package com.appfactory.route;

import java.util.List;

import com.appfactory.exceptions.MyException;
import com.appfactory.model.BluePrint;

public interface IappFactory {
	public List<String> doAction(BluePrint blueprint,String whatAction) throws MyException;
}
