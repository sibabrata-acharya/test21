
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       09-June-2016	 559296  Initial version
 */

package com.appmanagerserver.constants;

/**
 * @author 559296
 *
 */
public class ApplicationConstants {
	//For any api call
		public static final String BASE_URL ="/app";
		
		//For appfactory
		public static final String DEPLOYEMENT ="/appFactory";
		
		
		//authentication
		public static final String PAAS_AUTHENTICATION ="/paasauthenticate";
		
		//get catalog
		public static final String GET_ALL_CATALOG ="/paasCatalog";
		
		//
		public static final String GET_RELATIVE_CATALOG ="/getRelatedCatalog";

		public static final String SEARCH_CATALOG ="/searchCatalog";
		
		public static final String AUTH_CATEORY="/authCategory";

		public static final String JSON_CONTENT_TYPE = "application/json";
	    public static final String APPFACTORY_QUEUE_NAME = "app.appfactory.queue";

	    public static final String INTERNAL_SERVER_ERROR = "There is a internal server error.Please try again";
	    
	    public static final String RESPONSE_QUEUE ="/result";
	    public static final String SAVE_STATUS ="/status";

}
