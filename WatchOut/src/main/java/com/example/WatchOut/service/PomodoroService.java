package com.example.WatchOut.service;

import com.example.WatchOut.model.TimerResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PomodoroService {

    private final SimpMessagingTemplate messagingTemplate;
    private ScheduledExecutorService scheduler;
    private int timeLeft;
    private String currentPhase;

    public PomodoroService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void startSession(int seconds, String phase) {
        // Clear any existing timer thread to prevent overlaps
        stopTimer();

        this.timeLeft = seconds;
        this.currentPhase = phase;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            if (timeLeft >= 0) {
                // Broadcast current state to all subscribers at /topic/timer
                messagingTemplate.convertAndSend("/topic/timer",
                        new TimerResponse(timeLeft, currentPhase));
                timeLeft--;
            } else {
                stopTimer();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}
