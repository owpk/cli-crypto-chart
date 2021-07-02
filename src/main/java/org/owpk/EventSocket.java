package org.owpk;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
@Getter
@Setter
public class EventSocket {
    private final CountDownLatch closeLatch;
    @SuppressWarnings("unused")
    private Session session;
    private View<String> view;
    private MessageProvider messageProvider;

    public EventSocket(View<String> view, MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
        this.view = view;
        this.closeLatch = new CountDownLatch(1);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    public void awaitClose() throws InterruptedException {
        this.closeLatch.await();
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        try {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture(messageProvider.getMessage());
            fut.get(2, TimeUnit.SECONDS); // wait for send to complete.
        }
        catch (Throwable t) {
            view.handleError(t);
        }
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        view.handleData(msg);
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        view.handleError(cause);
    }
}