package com.devhat.estore.ProductsService.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="Product")
@Data
public class ProductEntity implements Serializable {
	
	private static final long serialVersionUID = -3599164136777217444L;
	
	@Id
	@Column(unique = true)
	private  String productId;
	@Column(unique = true)
	private  String title;
	private  Integer quantity;
	private  BigDecimal price;

}
