package net.folleach.dontaionalerts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.folleach.daintegrate.listeners.IListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class DonationAlertsClient {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private final Socket socket;
    private final URI url;
    private String currentToken;

    private final Emitter.Listener connectListener;
    private final Emitter.Listener disconnectListener;
    private final Emitter.Listener donationListener;
    private final Emitter.Listener errorListener;

    public DonationAlertsClient(String server, IListener<DonationAlertsEvent> eventListener) throws URISyntaxException {
        url = new URI(server);
        socket = IO.socket(url);

        connectListener = arg -> {
            System.out.println("connect");
        };

        disconnectListener = arg -> {
            System.out.println("disconnect");
        };

        donationListener = arg -> {
            if (arg.length < 1)
                return;
            var json = arg[0];
            if (!(json instanceof String))
                return;
            DonationAlertsEvent event = gson.fromJson((String)json, DonationAlertsEvent.class);
            eventListener.onValue(event);
        };

        errorListener = arg -> {
            System.out.println("error");
        };

        socket.on(Socket.EVENT_CONNECT, connectListener)
                .on(Socket.EVENT_DISCONNECT, disconnectListener)
                .on(Socket.EVENT_ERROR, errorListener)
                .on("donation", donationListener);
    }

    public boolean connect(String token) {
        currentToken = token;
        socket.connect();
        try {
            socket.emit("add-user", new JSONObject()
                    .put("token", currentToken)
                    .put("type", "minor"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void disconnect() {
        socket.close();
        currentToken = null;
    }

    public boolean getConnected() {
        return socket.connected();
    }
}
