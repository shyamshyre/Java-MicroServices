/**
 * 
 */
package com.devhat.estore.ProductsService.eventhandler.query.handler;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.devhat.estore.ProductsService.entity.ProductEntity;
import com.devhat.estore.ProductsService.eventhandler.query.FindProductsQuery;
import com.devhat.estore.ProductsService.query.model.ProductRestModel;
import com.devhat.estore.ProductsService.repository.ProductsRepository;

/**
 * @author shyam
 *
 */
@Component
public class ProductsQueryHandler {
	
	private final ProductsRepository productsRepository;
	public ProductsQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@QueryHandler
	public List<ProductRestModel> findProducts(FindProductsQuery findProductsQuery) {
		   List<ProductRestModel> productsRest = new ArrayList<>();
		   List<ProductEntity> storedProducts = productsRepository.findAll();
		   
		   for(ProductEntity entity: storedProducts) {
			   ProductRestModel productRestModel = new ProductRestModel();
			   BeanUtils.copyProperties(entity,productRestModel);
			   productsRest.add(productRestModel);
		   }
		   
		   return productsRest;
	}

}
