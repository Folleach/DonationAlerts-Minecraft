package net.folleach.dontaionalerts;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URI;
import java.net.URISyntaxException;

public class DonationAlertsClient {
    private Socket sock;
    private URI _url;
    private String _token;

    private Emitter.Listener connectListener;
    private Emitter.Listener disconnectListener;
    private Emitter.Listener donationListener;
    private Emitter.Listener errorListener;

    public DonationAlertsClient(String server) throws URISyntaxException {
        _url = new URI(server);
        sock = IO.socket(_url);

        connectListener = arg -> {

        };

        disconnectListener = arg -> {
        };

        donationListener = arg -> {
        };

        errorListener = arg -> {
        };

        sock.on(Socket.EVENT_CONNECT, connectListener)
                .on(Socket.EVENT_DISCONNECT, disconnectListener)
                .on(Socket.EVENT_ERROR, errorListener)
                .on("donation", donationListener);
    }

    public void Connect(String token) {
        _token = token;
        var connectionObject = new ConnectObject();
        connectionObject.type = "minor";
        connectionObject.token = _token;
        sock.connect();
        sock.emit("add-user", new Gson().toJson(connectionObject));
    }

    public void Disconnect() {
        sock.disconnect();
    }

    public boolean getConnected() {
        return sock.connected();
    }
}
