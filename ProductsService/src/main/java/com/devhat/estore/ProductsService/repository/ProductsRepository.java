/**
 * 
 */
package com.devhat.estore.ProductsService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devhat.estore.ProductsService.entity.ProductEntity;

/**
 * @author shyam
 *
 */
//Second type is the data type of primary ID field in the ProductEntity 
public interface ProductsRepository extends JpaRepository<ProductEntity, String>{
	ProductEntity findByProductId(String productId);
	ProductEntity findByProductIdOrTitle(String productId ,String title);
	//No need  of we writing the code as the spring will take care of it.

}
