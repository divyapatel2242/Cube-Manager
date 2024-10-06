package com.cube.manage.crm.esRepo;

import com.cube.manage.crm.esdocument.ProductEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEsRepository extends ElasticsearchRepository<ProductEs,String> {
}
