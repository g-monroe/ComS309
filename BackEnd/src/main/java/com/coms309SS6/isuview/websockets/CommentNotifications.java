package com.coms309SS6.isuview.websockets;

import com.coms309SS6.isuview.repository.CommentRepository;
import com.coms309SS6.isuview.service.CommentService;
import com.coms309SS6.isuview.websockets.config.CustomConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that handles all notifications Accepts a userID for each user session
 */
@ServerEndpoint(value = "/websocket/comment", configurator = CustomConfigurator.class)
@Component
public class CommentNotifications {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentService commentService;

    Session session;

    /* Map of sessions and users */
    private static List<Session> openSessions = new ArrayList<>();

    /**
     * On socket open, creates a session for specific userID value
     *
     * @param session = session created
     */
    @OnOpen
    public void open(Session session) {
        this.session = session;
        System.out.println("Socket opened");
        openSessions.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String commentID) {
        // List<String> userList = commentService.getAllFollowers(commentID);
        // System.out.println(commentID);

        // try{
        //     session.getBasicRemote().sendText(commentID);
        // }catch (Exception ex){}

        try {
            sendText(this.session, "this got sent");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (Session userSession : openSessions) {
            try {
                sendText(userSession, "Theres been an update to the comment you follow: "
                        + commentRepository.findById(commentID).get().getCommentText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a notification to all followers of specific comment
     *
     * @param commentID = commentID to find
     */

    /**
     * On socket close, kills specific session
     *
     * @param session
     */
    @OnClose
    public void close(Session session) {
        openSessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        openSessions.remove(session);
        sendText(session, "There was an error");
    }

    private void sendText(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}