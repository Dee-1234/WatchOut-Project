package com.example.WatchOut.config; // Change this from com.example.WatchOut.config

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Adding setAllowedOriginPatterns("*") helps prevent connection blocks
        registry.addEndpoint("/pomodoro-websocket")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}