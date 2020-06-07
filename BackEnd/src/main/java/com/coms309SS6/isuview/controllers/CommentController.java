package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.Comment;
import com.coms309SS6.isuview.repository.CommentRepository;
import com.coms309SS6.isuview.repository.UserRepository;
import com.coms309SS6.isuview.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles all api calls dealing with comments
 */
@RestController
public class CommentController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    /**
     * Saves passed comment
     *
     * @return void
     */
    @PostMapping(value = "/api/createcomment")
    public String saveComment(@RequestParam String userId, @RequestParam String building, @RequestParam String commentText) {
        return commentService.saveNewComment(building, userId, commentText);
    }

    /**
     * Update a given comment and notify followers
     *
     * @param commentId
     * @param commentText
     */
    @PostMapping("/api/updateComment")
    public void updateComment(@RequestParam String commentId, @RequestParam String commentText) {
        commentRepository.findById(commentId).get().setCommentText(commentText);
    }

    /**
     * Returns all comments from requested building
     *
     * @return List<String> A list of comments that have the requested building id
     */
    @GetMapping("/api/grabcomments/{building}")
    public List<Comment> buildingComments(@PathVariable String building) {
        List<Comment> commentList = commentService.getAllCommentsForBuilding(building);
        return commentService.createJsonForComments(commentList);
    }

    /**
     * Returns a comment to the user based on a given commentID
     *
     * @param commentId
     * @return
     */
    @GetMapping("/api/comment/{commentId}")
    public String getComment(@PathVariable String commentId) {
        return commentService.getCommentFromId(commentId);
    }

    /**
     * Returns a list of all followers to notify
     *
     * @return List<String> a list of followers to notify
     */
    // @GetMapping("/api/notifyfollowers") //Is this even needed?
    public List<String> notifyFollowers(String commentId) {
        return commentRepository.findById(commentId).get().getFollowers();
    }
}