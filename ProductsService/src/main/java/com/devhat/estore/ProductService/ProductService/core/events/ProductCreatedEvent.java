  package com.devhat.estore.ProductService.ProductService.core.events;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductCreatedEvent {
//	For using builder design pattern we need to have properties as the final,
//	but in this case we are using another way, so we are removing the final
	
	private  String productId;
	private  String title;
	private  BigDecimal price;
	private  Integer quantity;

}
