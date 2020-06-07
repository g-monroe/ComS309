package com.coms309SS6.isuview.service;

import com.coms309SS6.isuview.models.Comment;
import com.coms309SS6.isuview.repository.CommentRepository;
import com.coms309SS6.isuview.repository.UserRepository;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CommentService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    public String saveNewComment(String building, String userId, String commentText) {
        int randomInt = (int) (999999.0 * Math.random());
        commentRepository.save(new Comment(String.valueOf(randomInt), building, userId, commentText));
        return "{\n\"id\":" + String.valueOf(randomInt) + "\n}";
    }

    /**
     * Returns all comments for a building
     *
     * @param building
     * @return
     */
    public List<Comment> getAllCommentsForBuilding(String building) {
        return commentRepository.findByBuilding(building);
    }

    /**
     * Returns a list of all followers to notify
     *
     * @return List<String> a list of followers to notify
     */
    public List<String> getAllFollowers(String commentId) {
        return commentRepository.findById(commentId).get().getFollowers();
    }

    public String getCommentFromId(String commentId) {
        return commentRepository.findById(commentId).get().getCommentText();
    }

    /**
     * Creates a JSON for a given comment
     *
     * @return List<Comment> a list of comments in JSON format
     */
    public List<Comment> createJsonForComments(List<Comment> comments) {
        JSONObject responseDetailsJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        List<Comment> commentList = new ArrayList<Comment>();

        for (Comment c : comments) {
            commentList.add(c);
            JSONObject formDetailsJson = new JSONObject();

            try {
                formDetailsJson.put("id", c.getId());
                formDetailsJson.put("building", c.getBuilding());
                formDetailsJson.put("author", c.getAuthorID());
                formDetailsJson.put("text", c.getCommentText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        try {
            responseDetailsJson.put("comment", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentList;
    }
}