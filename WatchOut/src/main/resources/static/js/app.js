var stompClient = null;

/**
 * Connect to the Spring Boot WebSocket
 */
function connect() {
    console.log("Attempting to connect to WatchOut WebSocket...");

    // This must match the endpoint in your WebSocketConfig.java
    var socket = new SockJS('/pomodoro-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected to WatchOut: ' + frame);

        // Subscribe to the topic the server broadcasts on
        stompClient.subscribe('/topic/timer', function (sdkEvent) {
            const timerData = JSON.parse(sdkEvent.body);
            updateTimerUI(timerData.time, timerData.phase);
        });
    }, function (error) {
        console.error('WebSocket Error: ', error);
    });
}

/**
 * Update the HTML elements and handle the pop-up logic
 */
function updateTimerUI(seconds, phase) {
    const display = document.getElementById('timer-display');
    const phaseLabel = document.getElementById('phase-label');

    // Format seconds into MM:SS
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    const formattedTime = `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;

    display.innerText = formattedTime;
    phaseLabel.innerText = phase;

    // Trigger Pop-up logic exactly when timer hits 0
    if (seconds === 0) {
        triggerAlert(phase);
    }
}

/**
 * Handles the specific pop-up messages for WatchOut
 */
function triggerAlert(phase) {
    if (phase === "Work") {
        alert("Break time!");
    } else {
        alert("Break over! Ready to WatchOut?");
    }
}

/**
 * Sends a message to the Spring Boot @MessageMapping endpoints
 */
function sendSignal(type) {
    if (stompClient !== null && stompClient.connected) {
        console.log("Sending signal: " + type);
        stompClient.send("/app/start-" + type, {}, {});
    } else {
        console.error("Not connected to WebSocket!");
    }
}

// Execute connection on page load
connect();