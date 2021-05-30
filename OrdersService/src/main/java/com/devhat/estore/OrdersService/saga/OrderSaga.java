/**
 * 
 */
package com.devhat.estore.OrdersService.saga;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.devhat.estore.OrdersService.core.events.OrderCreatedEvent;
import com.devhat.estore.core.commands.ReserveProductCommand;

/**
 * @author shyam
 * This will handle the events and publishes commands
 * Saga is serialized , so to not to get the Gateway serialized mark it as transient. 
 */

//Application Flow.
//CreateOrderCommand
//OrderCreatedEvent->

@Saga
public class OrderSaga {
	
	@Autowired
	private transient CommandGateway commandGateway;
	
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand command = ReserveProductCommand.builder()
				.orderId(orderCreatedEvent.)
		
	}
	
	
	
	

}
