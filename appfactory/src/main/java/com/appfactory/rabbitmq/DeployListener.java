package com.appfactory.rabbitmq;


import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.appfactory.exceptions.CustomErrorMessage;
import com.appfactory.exceptions.MyException;
import com.appfactory.model.BluePrint;
import com.appfactory.resources.Messages;
import com.appfactory.route.IappFactory;
import com.appfactory.utils.ExceptionUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * /*** File : DeployListener.java 
 * Description : Mq Listener class and the message will be received on onMessage method
 *  Revision History :	Version	  Date			Author	Reason
 *   					0.1     27-June-2016	 559296 Initial version
 */
@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeployListener {
	/*
	 * it's a listener method which will receive message for further processing
	 * 
	 * @see org.springframework.amqp.core.MessageListener#onMessage(org.
	 * springframework.amqp.core.Message)
	 */
	    @Autowired
		private IappFactory apfactory;

		@Autowired
		private ExceptionUtils eUtils;
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${spring.rabbitmq.queuename}", durable = "false"), exchange = @Exchange(value = "${spring.rabbitmq.exchange}"), key = "${spring.rabbitmq.queuename}"))
	
	public void onMessage(Message arg0) throws MyException{

		String messageID = new String(arg0.getMessageProperties().getCorrelationId());
		String messageContent = new String(arg0.getBody());
		System.out.println("This is my message id  "+messageID);
		System.out.println("This is my message content  "+messageContent);
			
		ObjectMapper mapper = new ObjectMapper();

		BluePrint blueprint = new BluePrint();
		try {
			blueprint = mapper.readValue(messageContent,BluePrint.class);
		} catch (IOException e1) {
			e1.printStackTrace();
			eUtils.myException(CustomErrorMessage.ERROR_PARSING_JSON, e1.getMessage());
		}
			System.out.println(blueprint.getAccesstoken());
		apfactory.doAction(blueprint, Messages.getString("FactoryController.2"));
		apfactory.doAction(blueprint, Messages.getString("FactoryController.3"));
	}
}
