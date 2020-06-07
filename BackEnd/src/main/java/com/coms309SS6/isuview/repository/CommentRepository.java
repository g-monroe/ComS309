package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Comment Repository for Comment Entity
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByBuilding(String building);

    List<Comment> findByAuthorID(String authorID);

    Optional<Comment> findById(String id);
}