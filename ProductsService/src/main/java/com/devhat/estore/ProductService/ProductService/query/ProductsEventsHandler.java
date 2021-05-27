package com.devhat.estore.ProductService.ProductService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
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
	@EventHandler
	public void on(ProductCreatedEvent event) {
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

	}

}
	