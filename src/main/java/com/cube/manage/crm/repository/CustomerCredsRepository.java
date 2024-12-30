package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.UserCreds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCredsRepository extends JpaRepository<UserCreds,Integer> {

    @Query(value = "SELECT * FROM cube.user_creds where user_name=?",nativeQuery = true)
    UserCreds findByUserName(String username);
}
