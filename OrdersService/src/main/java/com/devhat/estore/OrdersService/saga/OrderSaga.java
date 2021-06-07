/**
 * 
 */
package com.devhat.estore.OrdersService.saga;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.devhat.estore.OrdersService.core.events.OrderCreatedEvent;
import com.devhat.estore.core.commands.ProductReservedEvent;
import com.devhat.estore.core.commands.ReserveProductCommand;

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
					// Start a compensating Transaction.
				}

			}

		});

	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		logger.info("SagaEventHandler--> ProductReservedEvent--> associationProperty->orderId ");
		
		// Process the Payment

	}

}
