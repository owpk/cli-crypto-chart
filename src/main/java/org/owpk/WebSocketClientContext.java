package org.owpk;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class WebSocketClientContext {
    private URI host;
    private WebSocketClient client;
    private EventSocket eventSocket;

    public WebSocketClientContext(String uri, View<String> view) {
        this.eventSocket = view.getEventHandler();
        try {
            client = new WebSocketClient();
            host = new URI(uri);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void connect() {
        try {
            client.start();
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(eventSocket, host, request);
            eventSocket.awaitClose();
//            eventSocket.awaitClose(15, TimeUnit.SECONDS);
        } catch (IOException e) {
            System.out.println("connection reset");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
               client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
