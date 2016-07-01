package com.appfactory.constants;

import com.appfactory.resources.Messages;
/**
 * File : ApplicationConstants.java 
 * Description : This class is designed to 
 * store all the constants
 * Revision History : 
 * @author 559296
 *
 */
public class ApplicationConstants {
	public static final String BASE_URL_ONEC = Messages.getString("OneC_API_EndPoint");
	public static final String BASE_URL_PIVOTAL = Messages.getString("Pivotal_API_EndPoint");
	public static final String BASE_URL_BLUEMIX = Messages.getString("Bluemix_API_EndPoint"); 
	public static final String BASE_URL_AZURE = Messages.getString("Azure_API_EndPoint");
	public static final String GET_REQUEST = "GET";
	public static final String POST_REQUEST = "POST";
	public static final String PLATFORM_CREATEAPP = "App is created and ready to push";
	public static final String PLATFORM_GETDOMAINS="To push the app we have got the domain details";
	public static final String PLATFORM_CREATEROUTE="We have created a route from where application can be accessed";
	public static final String INTERNAL_SERVER_ERROR = "The server encountered an internal error. Please retry the request.";
	public static final String PLATFORM_ASSOCIATEROUTE="Route is associated with the app";
	public static final String PLATFORM_UPLOADPP="Application is pushed to the cloud foundry";
	public static final String PLATFORM_STARTAPP="Your application has been started";
	/**
	 * Creating constant variable for application/json
	 */
	public static final String JSON_CONTENT_TYPE = "application/json";
	public static final String ACTUAL_RESPONSE_QUEUE = "app.managerserver.response.queue";
}
