/**
 * 
 */
package com.devhat.estore.OrdersService.saga;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.devhat.estore.OrdersService.core.events.OrderCreatedEvent;
import com.devhat.estore.core.commands.ProductReservedEvent;
import com.devhat.estore.core.commands.ReserveProductCommand;
import com.devhat.estore.core.model.User;
import com.devhat.estore.core.query.FetchUserPaymentDetailsQuery;

/**
 * @author shyam This will handle the events and publishes commands Saga is
 *         serialized , so to not to get the Gateway serialized mark it as
 *         transient.
 */

//Application Flow.
//CreateOrderCommand
//OrderCreatedEvent->

@Saga
public class OrderSaga {

	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway; 
	private static final Logger logger = LoggerFactory.getLogger(OrderSaga.class);

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		logger.info("SagaEventHandler--> orderCreatedEvent--> associationProperty->orderId ");
		ReserveProductCommand command = ReserveProductCommand.builder().orderId(orderCreatedEvent.getOrderId())
				.productId(orderCreatedEvent.getProductId()).quantity(orderCreatedEvent.getQuantity())
				.userId(orderCreatedEvent.getUserId()).build();
		commandGateway.send(command, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				if (commandResultMessage.isExceptional()) {
					logger.info("SagaEventHandler--> orderCreatedEvent-> Compensation");
					logger.info("Start the  Compensation");
					// Start a compensating Transaction.
				}

			}

		});

	}

	
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		logger.info("SagaEventHandler--> ProductReservedEvent--> associationProperty->orderId ");
		logger.info("ProductReservedEvent is called for productId: "+ productReservedEvent.getProductId()+
				"OrderID"+productReservedEvent.getOrderId());
		// Process the Payment
		User userPaymentDetails =null;
		try {
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
		userPaymentDetails=queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			return;
			
		}
		if(userPaymentDetails == null)
		{
			//Start initiating the compensation transaction.
		}
		logger.info("Successfully fetched user payment details for user : "+userPaymentDetails.getFirstName());

	}

}
