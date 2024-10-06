package com.cube.manage.crm.esRepo;

import com.cube.manage.crm.esdocument.ProductItemEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemEsRepository extends ElasticsearchRepository<ProductItemEs,Integer> {
}
