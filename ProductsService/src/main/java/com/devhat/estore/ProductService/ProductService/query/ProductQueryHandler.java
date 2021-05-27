package com.devhat.estore.ProductService.ProductService.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductService.ProductService.core.data.ProductEntity;
import com.devhat.estore.ProductService.ProductService.core.data.ProductsRepository;
import com.devhat.estore.ProductService.ProductService.query.rest.ProductRestModel;

@Component
public class ProductQueryHandler {

	private final ProductsRepository productsRepository;	
	
	public ProductQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery){
		List<ProductRestModel> productsRest = new ArrayList<ProductRestModel>();
		List<ProductEntity> storedProducts = productsRepository.findAll();
		for(ProductEntity productEntity: storedProducts) {
			ProductRestModel productRestModel = new ProductRestModel();
			BeanUtils.copyProperties(productEntity, productRestModel);
			productsRest.add(productRestModel);
		}
		return productsRest;
	}
}
