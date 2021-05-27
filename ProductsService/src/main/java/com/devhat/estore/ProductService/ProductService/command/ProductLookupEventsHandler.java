package com.devhat.estore.ProductService.ProductService.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductService.ProductService.core.data.ProductLookupEntity;
import com.devhat.estore.ProductService.ProductService.core.data.ProductLookupRepository;
import com.devhat.estore.ProductService.ProductService.core.events.ProductCreatedEvent;


//The below annotation @(Processing-group) , it is used to logically group event handlers together.
//As we have two event handlers created for createProduct we are logically grouping 
//under one group.(Products Events Handler , Product Lookupevent handler)
@Component
@ProcessingGroup("process-group")
public class ProductLookupEventsHandler {
	private final ProductLookupRepository productLookupRepository;
	
	public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}
	
	@EventHandler
	public void on (ProductCreatedEvent productCreatedEvent) {
		ProductLookupEntity productLookupEntity = new ProductLookupEntity(productCreatedEvent.getProductId(),productCreatedEvent.getTitle());
		productLookupRepository.save(productLookupEntity);
		
	}

}
