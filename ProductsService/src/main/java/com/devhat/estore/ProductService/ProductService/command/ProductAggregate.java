package com.devhat.estore.ProductService.ProductService.command;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.devhat.estore.ProductService.ProductService.core.events.ProductCreatedEvent;

@Aggregate
public class ProductAggregate {

	@AggregateIdentifier	
	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
//	This helps in creating new instance of the class.
	public ProductAggregate() {
		
	}
	
	@CommandHandler
	public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {
		// Validate the Create Product Command.
		if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <=0) {
			throw new IllegalArgumentException("Price cannot be less than or equal to zero");
		}
		
		if (createProductCommand.getTitle() ==null) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		
		ProductCreatedEvent productcreatedEvent = new ProductCreatedEvent();
//		BeanUtils are copying the properties from source to destination.
//		Source : createProductCommand, Destination : ProductCreatedEvent
		BeanUtils.copyProperties(createProductCommand, productcreatedEvent);
		
		AggregateLifecycle.apply(productcreatedEvent);
		
//		We are intentionally throwing the exception from command handler and segregating
//		the handling from other exceptions.
//		Exceptions that arise in the command handlers and Query handlers are to be
//		treated in a separate way. 
//		In a distributed system its always useful to segregate the command and query exceptions.  

//		Even though the exception is raised after the apply method, still the event
//		wont be persisted in the event store.When apply method is called axon framework 
//		Doesn't immediately persist it. but only stages the event for execution. As the error 
//		is thrown then the transaction will  rollback and none of the events will be processed.
		
//		When an exception is raised from the command handler/query handler , Axon Framework
//		will robe them into Commandexecution or QueryExecution Exception.
//		
		if(true) {
			throw new Exception("Everything turned to be succesfull");
		}
		
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.title = productCreatedEvent.getTitle();
		this.quantity = productCreatedEvent.getQuantity();
		
	}
	
}


