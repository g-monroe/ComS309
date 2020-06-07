package com.coms309SS6.isuview.websockets;

import com.coms309SS6.isuview.websockets.config.CustomConfigurator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test endpoint for our websockets
 */
@ServerEndpoint(value = "/websocket/echo", configurator = CustomConfigurator.class)
@Component
@EnableScheduling
public class Echo {
    private static List<Session> sessionList = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sendText(session, "Connected!");
        sessionList.add(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        sendText(session, "You sent: " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        //sendText(session, "Disconnected!");
        sessionList.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        sendText(session, "Sorry, an error occured.");
    }

    @Scheduled(fixedRate = 5000)
    public void onFiveSeconds() throws IOException {
        for (Session session : sessionList) {
            try {
                sendText(session, "Ping!");
            } catch (Exception e) {
                sessionList.remove(session);
            }
        }
    }

    private void sendText(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
