package com.coms309SS6.isuview.models;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class User {
    @Id
    @Column(length = 32)
    private int id;
    private String name;
    @ElementCollection
    private List<String> following;

    /**
     * Gets Name
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Nmae
     *
     * @param name string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets userId
     *
     * @return String userId
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id
     *
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    public List<String> getFollowing() {
        return this.following;
    }

    public void addFollow(String commentID) {
        this.following.add(commentID);
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}