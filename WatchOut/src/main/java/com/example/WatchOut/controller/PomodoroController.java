package com.example.WatchOut.controller;

import com.example.WatchOut.service.PomodoroService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PomodoroController {

    private final PomodoroService pomodoroService;

    public PomodoroController(PomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;
    }

    @MessageMapping("/start-work")
    public void startWork() {
        pomodoroService.startSession(20, "Work");
    }

    @MessageMapping("/start-short-break")
    public void startShort() {
        pomodoroService.startSession(5, "Short Break");
    }

    @MessageMapping("/start-long-break")
    public void startLong() {
        pomodoroService.startSession(15, "Long Break");
    }
}
