package com.devhat.estore.ProductService.ProductService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductService.ProductService.core.data.ProductEntity;
import com.devhat.estore.ProductService.ProductService.core.data.ProductsRepository;
import com.devhat.estore.ProductService.ProductService.core.events.ProductCreatedEvent;

@Component
@ProcessingGroup("product-group")



public class ProductsEventsHandler {
	Logger logger = LoggerFactory.getLogger(ProductsEventsHandler.class);
	private final ProductsRepository productsRepository;
	public ProductsEventsHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	
//	This message will handle only exception that are thrown in the Eventhandler class.
//	It cannot handle exceptions that are thrown in the same class outside of the Eventhandlers.
	@ExceptionHandler(resultType=IllegalArgumentException.class)
	public void handle(IllegalArgumentException argumentException) {
		
	}
	
//	We are handling the below thrown exception and we are rethrowing it to the ProductServiceEventErrorHandler
	
	
	@ExceptionHandler(resultType=Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}
	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception{
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(event, productEntity);
		try {
			logger.info("Before Record Persistence");
			productsRepository.save(productEntity);
			logger.info("After Record Persistence");
		} catch (IllegalArgumentException ex) {
			logger.info(ex.toString());
			ex.printStackTrace();
		}

		if(true) {
			throw new Exception("Everything turned to be succesfull");
		}
	}

}
	