package com.cube.manage.crm.esRepo;

import com.cube.manage.crm.esdocument.CustomerEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerEsRepository extends ElasticsearchRepository<CustomerEs,Integer> {
}
