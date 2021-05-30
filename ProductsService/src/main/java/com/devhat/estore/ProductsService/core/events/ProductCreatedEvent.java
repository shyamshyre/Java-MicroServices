/**
 * 
 */
package com.devhat.estore.ProductsService.core.events;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author shyam
 * This varies from application to application what we send in event.
 * Here trying to persist all the information in read-database.
 */
@Data
public class ProductCreatedEvent {
	private  String productId;
	private  String title;
	private  Integer quantity;
	private  BigDecimal price;


}
