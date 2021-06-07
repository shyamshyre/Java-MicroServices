package com.devhat.estore.ProductsService.eventhandler.query.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductsService.core.events.ProductCreatedEvent;
import com.devhat.estore.ProductsService.entity.ProductEntity;
import com.devhat.estore.ProductsService.repository.ProductsRepository;
import com.devhat.estore.core.commands.ProductReservedEvent;

/**
 * This class is aloso called as ProjectionClass 
 * @author shyam
 * This class is updating the READ Database
 *
 */

@Component
@ProcessingGroup("product-group")
public class ProductsEventsHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsEventsHandler.class);
	private final ProductsRepository  productsRepository;
	
	public ProductsEventsHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	
	@ExceptionHandler(resultType = Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	// Either not handle exception or handle and throw it back to source.
	}
	
	@ExceptionHandler(resultType = IllegalArgumentException.class)
	public void handle(IllegalArgumentException argumentException) {
		
	}
	
	/**
	 * Handling the Exceptions in the Event Handler Methods.
	 * The above method will handle only internal exceptions raised within the class.
	 * if all other exceptions are to be handled then we need to  handle separately.
	 * Use ListenerInvocation Error handler to handle the exceptions externally from
	 * all other Exception within the event handlers.
	 * @param productCreatedEvent
	 * @throws Exception 
	 * 
	 */
	
	
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) throws Exception {
		LOGGER.info("Inside Query-ProductCreatedEvent");
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		try {
		productsRepository.save(productEntity);
		}catch(IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		LOGGER.info("Inside Query-ProductCreatedEvent-> RecordSaved to ReadDatabase");
//		if(true) throw new Exception("Forcing exception in the Event Handler Class");
		
	}

	
	/**
	 * Updating the ReadDatabase upon receiving the product Reserved Event
	 * @param productReservedEvent
	 * @throws Exception
	 */
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) throws Exception {
		LOGGER.info("Inside Query-ProductCreatedEvent");
		ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		try {
			productsRepository.save(productEntity);
			}catch(IllegalArgumentException ex) {
				ex.printStackTrace();
			}
		
	}
	
}
