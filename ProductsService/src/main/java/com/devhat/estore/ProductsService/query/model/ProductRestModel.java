package com.devhat.estore.ProductsService.query.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductRestModel {
	private  String productId;
	private  String title;
	private  Integer quantity;
	private  BigDecimal price;
}
