
/**
 * File : 
 * Description : 
 * Revision History :	Version  	Date		 Author	 Reason
 *   					0.1       27-June-2016	 559296  Initial version
 */

package com.appmanagerserver.producer;

import javax.servlet.jsp.jstl.sql.Result;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.appmanagerserver.blueprints.AppfactoryResponse;
import com.appmanagerserver.constants.ApplicationConstants;
import com.appmanagerserver.exception.MyException;
import com.appmanagerserver.status.AppfactoryService;
import com.appmanagerservice.sendinterface.IMessagePublisher;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * @author 559296
 *
 */
@CrossOrigin
@RestController
@RequestMapping(ApplicationConstants.BASE_URL)
public class MessageSender {
	@Autowired
	private IMessagePublisher messagePublisher;
	@Autowired
	private AppfactoryService factoryservice;
		
		
	/**
	 * 
	 * @param blueprint
	 * @return
	 * @throws MyException
	 */
		@SuppressWarnings("unchecked")
		@RequestMapping(value = ApplicationConstants.DEPLOYEMENT, method = RequestMethod.POST,headers="Accept=application/json")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
		         @ApiResponse(code = 401, message = "Unauthorized"), 
		         @ApiResponse(code = 500, message = "Failure") })
		public  ResponseEntity<JSONObject> appFactory(@RequestBody JSONObject blueprint) throws MyException {
			String requestUUID =messagePublisher.publishMessage(blueprint);
			final JSONObject responseobj = new JSONObject();
			responseobj.put("key", requestUUID);
			return new ResponseEntity<JSONObject>(responseobj, HttpStatus.OK);
			
		}
		
		@RequestMapping(value = ApplicationConstants.SAVE_STATUS, method = RequestMethod.POST,headers="Accept=application/json")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Result.class),
		         @ApiResponse(code = 401, message = "Unauthorized"), 
		         @ApiResponse(code = 500, message = "Failure") })
		public  ResponseEntity<AppfactoryResponse> saveStatus(@RequestBody String bluePrintID) throws MyException {
		
			final HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<AppfactoryResponse>(factoryservice.deployStatus(bluePrintID),httpHeaders,HttpStatus.OK);
			
		}
}
