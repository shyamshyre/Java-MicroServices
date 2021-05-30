/**
 * 
 */
package com.devhat.estore.ProductsService.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devhat.estore.ProductsService.eventhandler.query.FindProductsQuery;
import com.devhat.estore.ProductsService.query.model.ProductRestModel;

/**
 * @author shyam
 *
 */
@RestController
@RequestMapping("/products")
public class ProductQueryController {
	@Autowired
	QueryGateway queryGateway;
	
	@GetMapping
	public List<ProductRestModel> getProducts() {
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<ProductRestModel>  products= queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
		return products;
}
}