package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment Entity
 */
@Entity
public class Comment {
    @Id
    @Column(length = 32)
    private String id;
    private String building;
    private String authorID;
    private String commentText;
    @ElementCollection
    private List<String> followers = new ArrayList<String>();

    public Comment(String id, String building, String authorID, String commentText) {
        this.id = id;
        this.building = building;
        this.authorID = authorID;
        this.commentText = commentText;
    }

    public Comment() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public List<String> getFollowers() {
        return this.followers;
    }

    public void addFollower(String follower) {
        this.followers.add(follower);
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}