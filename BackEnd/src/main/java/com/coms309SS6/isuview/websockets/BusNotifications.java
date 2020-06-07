package com.coms309SS6.isuview.websockets;

import com.coms309SS6.isuview.models.BusRoute;
import com.coms309SS6.isuview.models.BusStop;
import com.coms309SS6.isuview.repository.BusRouteRepository;
import com.coms309SS6.isuview.repository.BusStopRepository;
import com.coms309SS6.isuview.service.BusService;
import com.coms309SS6.isuview.websockets.config.CustomConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Endpoint allowing bus notifications to be sent out
 */
@ServerEndpoint(value = "/websocket/bus", configurator = CustomConfigurator.class)
@Component
@EnableScheduling
public class BusNotifications {
    private static Map<Session, Map<Time, String>> sessionNotificationMap = new HashMap<>();
    private static long fiveMinutes = 300000;
    private static long twoMinutes = 120000;

    @Autowired
    BusService busService;

    @Autowired
    BusRouteRepository busRouteRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessionNotificationMap.put(session, new HashMap<>());
    }

    /**
     * Callback that is triggered when a message is sent to the server. These messages should be sent in the format
     * "Bus Route::Bus Stop", for instance "9 Plum::Kildee Hall" would trigger notifications for the next 9 Plum bus
     * for Kildee Hall.
     * <p>
     * This will return when the next bus will arrive at the stop as a message to the websocket, along with a
     * notifications 5 minutes and 2 minutes away from arrival.
     *
     * @param session
     * @param message
     * @throws IOException
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String[] params = message.split("::");
        if (params.length != 2) return;

        Optional<BusRoute> busRoute = busRouteRepository.findByName(params[0]);
        if (!busRoute.isPresent()) return;
        Optional<BusStop> busStop = busStopRepository.findByName(params[1]);
        if (!busStop.isPresent()) return;

        Time earliestTime = busService.getEarliestArrivableTime(busRoute.get().getId(), busStop.get().getId());
        if (earliestTime == null) return;

        String notificationMessage = "The next " + params[0] + " bus will arrive at " + params[1] + " at " + BusService.getReadableTime(earliestTime) + ".";
        sendText(session, notificationMessage);

        Time fiveMinBefore = new Time(earliestTime.getTime() - fiveMinutes);
        if (Time.valueOf(LocalTime.now()).before(fiveMinBefore)) {
            // add notification
            notificationMessage = "Arriving in 5 minutes: " + params[0] + " at " + params[1] + ".";
            sessionNotificationMap.get(session).put(fiveMinBefore, notificationMessage);
        }

        Time twoMinBefore = new Time(earliestTime.getTime() - twoMinutes);
        if (Time.valueOf(LocalTime.now()).before(twoMinBefore)) {
            // add notification
            notificationMessage = "Arriving in 2 minutes: " + params[0] + " at " + params[1] + "!";
            sessionNotificationMap.get(session).put(twoMinBefore, notificationMessage);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessionNotificationMap.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        sessionNotificationMap.remove(session);
        sendText(session, "Knock knock! Who's there? An internal server error!");
    }

    /**
     * Scheduled method that is called every minute in order to send periodic notifications that we scheduled.
     */
    @Scheduled(fixedRate = 60000)
    public void onMinute() {
        for (Map.Entry<Session, Map<Time, String>> entry : sessionNotificationMap.entrySet()) {
            for (Map.Entry<Time, String> timeStringEntry : entry.getValue().entrySet()) {
                // check if our time is within 30 seconds of the time we want to send
                if (Math.abs(timeStringEntry.getKey().getTime() - Time.valueOf(LocalTime.now()).getTime()) < 31 * 1000) {
                    try {
                        sendText(entry.getKey(), timeStringEntry.getValue());
                    } catch (Exception e) {
                        sessionNotificationMap.get(entry.getKey()).remove(timeStringEntry.getKey());
                    }
                }
            }
        }
    }

    private void sendText(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}
