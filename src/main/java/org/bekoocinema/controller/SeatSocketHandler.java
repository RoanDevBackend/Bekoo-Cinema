package org.bekoocinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.request.booking.SeatSelectedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SeatSocketHandler extends TextWebSocketHandler {

    final ObjectMapper objectMapper;
    static final Map<String, List<WebSocketSession>> sessionInShowtimeMap = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String showtimeId = extractShowtimeIdFromQuery(Objects.requireNonNull(session.getUri()).getQuery());
        if (showtimeId != null) {
            if(sessionInShowtimeMap.containsKey(showtimeId)) {
                sessionInShowtimeMap.get(showtimeId).add(session);
            }else {
                List<WebSocketSession> sessionList = new ArrayList<>();
                sessionList.add(session);
                sessionInShowtimeMap.put(showtimeId, sessionList);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String showtimeId = extractShowtimeIdFromQuery(Objects.requireNonNull(session.getUri()).getQuery());
        if (showtimeId != null) {
            if(sessionInShowtimeMap.containsKey(showtimeId)) {
                sessionInShowtimeMap.get(showtimeId).remove(session);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        SeatSelectedRequest seatSelectedRequest = objectMapper.readValue(payload, SeatSelectedRequest.class);
        List<WebSocketSession> sessionInShowtimeList = sessionInShowtimeMap.get(seatSelectedRequest.getShowtimeId());
        for(WebSocketSession sessionInShowtimeItem : sessionInShowtimeList) {
            if(!sessionInShowtimeItem.getId().equals(session.getId()))
                sessionInShowtimeItem.sendMessage(new TextMessage(objectMapper.writeValueAsString(seatSelectedRequest)));
            else {
                sessionInShowtimeItem.sendMessage(new TextMessage("You are selected seat"));
            }
        }
    }

    //Helper function
    private String extractShowtimeIdFromQuery(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("showtimeId")) {
                return pair[1];
            }
        }
        return null;
    }

}
