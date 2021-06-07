/**
 * 
 */
package com.devhat.estore.ProductsService.aggregate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.devhat.estore.ProductsService.command.CreateProductCommand;
import com.devhat.estore.ProductsService.core.events.ProductCreatedEvent;
import com.devhat.estore.core.commands.ProductReservedEvent;
import com.devhat.estore.core.commands.ReserveProductCommand;

/**
 * @author shyam
 *Aggregate class holds the current state of the object it the heart of the Application.
 *In addition to it it has methods to perform validations.
 *Aggregate holds following information.
 * 1) Product State.
 * 2) Command Handlers.
 * 3) Business Logic. - Validations
 * 4) Event Handlers. - Event Sourcing Handlers. Loads the changes from Event Store.
 */

//The below annotation specifies its a aggregate

@Aggregate 
public class ProductAggregate {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductAggregate.class);
	
	//Axon framework will use this identifier to associate this command with 
	// aggregate object in your application.

	private  String title;
	private  Integer quantity;
	private  BigDecimal price;
	@AggregateIdentifier
	private  String productId;
	
	public ProductAggregate() {
		
		
	}
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand)  throws Exception {
		LOGGER.info("Indide the ProductAggregate CommandHadler");
		//Command validation - Validate CreateProductCommand
		
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
//		This will copy all the properties till they match
		BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
		AggregateLifecycle.apply(productCreatedEvent);
		
//		if(true) throw new Exception("An error took place int he Command Handler, it will not allow the flow further. even thought lifecycle is called");
		
	}


	@CommandHandler
	public ProductAggregate(ReserveProductCommand reserveProductCommand)  throws Exception {
		LOGGER.info("Indide the ProductAggregate ReserveProductCommand");
		//Command validation - Validate CreateProductCommand
		if(quantity < reserveProductCommand.getQuantity()) {
			throw new IllegalStateException("Insuffecient number of items in the Stock");
		}
		ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
				.orderId(reserveProductCommand.getOrderId())
				.productId(reserveProductCommand.getProductId())
				.quantity(reserveProductCommand.getQuantity())
				.userId(reserveProductCommand.getUserId()).build();
		
		AggregateLifecycle.apply(productReservedEvent);
	}



// This will aggregate the state with new information with the changes done in handler.
@EventSourcingHandler
public void on(ProductCreatedEvent productCreatedEvent) {
	LOGGER.info("Inside the ProductAggregate ProductCreatedEvent ");
//	Avoid using business logic, just update state.
	this.productId =  productCreatedEvent.getProductId();
	this.quantity = productCreatedEvent.getQuantity();
	this.price =  productCreatedEvent.getPrice();
	this.title = productCreatedEvent.getTitle();
	
}


@EventSourcingHandler
public void on(ProductReservedEvent  productReservedEvent) {
	LOGGER.info("Inside the ProductAggregate productReservedEvent ");
	this.quantity = this.quantity - productReservedEvent.getQuantity();
	
}

}
