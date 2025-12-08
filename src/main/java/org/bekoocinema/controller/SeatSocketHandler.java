package org.bekoocinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.request.booking.SeatSelectedRequest;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
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
    static final Map<String, Map<String, List<String>>> selectedSeatMap = new ConcurrentHashMap<>(); // showtimeId, sessionId, seatIdSelected

    @Override
    @SneakyThrows
    public void afterConnectionEstablished(WebSocketSession session) {
        String showtimeId = extractShowtimeIdFromQuery(Objects.requireNonNull(session.getUri()).getQuery());
        if (showtimeId != null) {
            if(sessionInShowtimeMap.containsKey(showtimeId)) {
                sessionInShowtimeMap.get(showtimeId).add(session);
            }else {
                List<WebSocketSession> sessionList = new ArrayList<>();
                sessionList.add(session);
                sessionInShowtimeMap.put(showtimeId, sessionList);
                selectedSeatMap.put(showtimeId, new ConcurrentHashMap<>());
            }
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(this.getSeatsSelected(showtimeId))));
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
        if(selectedSeatMap.containsKey(showtimeId)){
            selectedSeatMap.get(showtimeId).remove(session.getId());
        }
        TextMessage listSelectedSeat = new TextMessage(objectMapper.writeValueAsString(this.getSeatsSelected(showtimeId)));
        for(WebSocketSession sessionInShowtime : sessionInShowtimeMap.get(showtimeId)){
            sessionInShowtime.sendMessage(listSelectedSeat);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        SeatSelectedRequest seatSelectedRequest = objectMapper.readValue(payload, SeatSelectedRequest.class);
        List<WebSocketSession> sessionInShowtimeList = sessionInShowtimeMap.get(seatSelectedRequest.getShowtimeId());
        Map<String, List<String>> selectedSeatBySessionMap = selectedSeatMap.get(seatSelectedRequest.getShowtimeId());
        if(!selectedSeatBySessionMap.containsKey(session.getId()) && seatSelectedRequest.isSelected()){
            List<String> selectedIdBySessionList = new ArrayList<>();
            selectedIdBySessionList.add(seatSelectedRequest.getSeatId());
            selectedSeatBySessionMap.put(session.getId(), selectedIdBySessionList);
        }else {
            if(selectedSeatBySessionMap.containsKey(session.getId())){
                if(seatSelectedRequest.isSelected()){
                    selectedSeatBySessionMap.get(session.getId()).add(seatSelectedRequest.getSeatId());
                }else {
                    selectedSeatBySessionMap.get(session.getId()).remove(seatSelectedRequest.getSeatId());
                }
            }
        }
        for(WebSocketSession sessionInShowtimeItem : sessionInShowtimeList) {
            if(!sessionInShowtimeItem.getId().equals(session.getId()))
                sessionInShowtimeItem.sendMessage(new TextMessage(objectMapper.writeValueAsString(seatSelectedRequest)));
            else {
                sessionInShowtimeItem.sendMessage(new TextMessage("You are selected seat"));
            }
        }
    }

    @EventListener
    @SneakyThrows
    public void onSeatRelease(PayloadApplicationEvent<Map<String, Object>> event) {
        Map<String, Object> payload = event.getPayload();
        String showtimeId = payload.containsKey("showtimeId") ? (String) payload.get("showtimeId") : null;
        List<String> seats = payload.containsKey("seatIdsSelected") ? (List<String>) payload.get("seatIdsSelected") : null;
        if(showtimeId == null || seats == null || seats.isEmpty()){
            return;
        }
        List<WebSocketSession> sessions = sessionInShowtimeMap.get(showtimeId);
        if (sessions == null) return;
        String json = objectMapper.writeValueAsString(seats);
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(json));
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

    private List<String> getSeatsSelected(String showtimeId){
        List<List<String>> selectedBySession = selectedSeatMap.get(showtimeId).values().stream().toList();
        List<String> seatSelectedResponse = new ArrayList<>();
        for(List<String> selectedSeat : selectedBySession) {
            seatSelectedResponse.addAll(selectedSeat);
        }
        return seatSelectedResponse;
    }
}
