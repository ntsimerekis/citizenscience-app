package com.tsimerekis.user.repository;

import com.tsimerekis.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {

    public UserEntity findByUsername(String username);

    public boolean existsByUsername(String username);

}
