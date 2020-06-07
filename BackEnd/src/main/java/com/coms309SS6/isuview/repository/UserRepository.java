package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User Repository for User Entity
 */
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByName(String name);
}