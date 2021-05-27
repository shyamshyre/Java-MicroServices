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
	public ProductAggregate(CreateProductCommand createProductCommand) {
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
		
	}
	
	@EventSourcingHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		this.productId = productCreatedEvent.getProductId();
		this.price = productCreatedEvent.getPrice();
		this.title = productCreatedEvent.getTitle();
		this.quantity = productCreatedEvent.getQuantity();
		
	}
	
}
