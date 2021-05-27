/**
 * 
 */
package com.devhat.estore.ProductService.ProductService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shyam
 *
 */
public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
 ProductLookupEntity findByProductIdOrTitle(String productId, String title); 
 
	
}
