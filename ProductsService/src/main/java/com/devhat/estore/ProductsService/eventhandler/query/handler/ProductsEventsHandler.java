package com.devhat.estore.ProductsService.eventhandler.query.handler;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductsService.core.events.ProductCreatedEvent;
import com.devhat.estore.ProductsService.entity.ProductEntity;
import com.devhat.estore.ProductsService.repository.ProductsRepository;

/**
 * This class is aloso called as ProjectionClass 
 * @author shyam
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
	
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		LOGGER.info("Inside Query-ProductCreatedEvent");
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		productsRepository.save(productEntity);
		LOGGER.info("Inside Query-ProductCreatedEvent-> RecordSaved to ReadDatabase");
	}

}
