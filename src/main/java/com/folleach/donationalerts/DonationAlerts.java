package com.folleach.donationalerts;

import java.net.URI;
import java.net.URISyntaxException;

import com.folleach.daintegrate.DonationAlertsIntegrate;
import org.json.JSONException;
import org.json.JSONObject;

import com.folleach.daintegrate.Main;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import net.minecraft.client.resources.I18n;

public class DonationAlerts {
	private Socket sock;
	private URI _url;
	private String _token;

	private Listener connectListener;
	private Listener disconnectListener;
	private Listener donationListener;
	private Listener errorListener;

	public DonationAlerts(String server, DonationAlertsIntegrate donationAlertsIntegrate) throws URISyntaxException {
		_url = new URI(server);
		sock = IO.socket(_url);

		connectListener = new Listener() { @Override
			public void call(Object... arg0) {
				Main.DonationAlertsInformation(I18n.format("daintegratew.connected"));
			}
		};
		
		disconnectListener = new Listener() {
			@Override
			public void call(Object... arg0) {
				Main.DonationAlertsInformation(I18n.format("daintegratew.disconnected"));
			}
		};

		errorListener = new Listener() {
			@Override
			public void call(Object... arg0) {
				Main.DonationAlertsInformation(I18n.format("daintegratew.error"));
			}
		};

		donationListener = new Listener() {
			@Override
			public void call(Object... arg0) {
				donationAlertsIntegrate.addEvent(DonationAlertsEvent.getDonationAlertsEvent((String)arg0[0]));
			}
		};

		sock.on(Socket.EVENT_CONNECT, connectListener)
		.on(Socket.EVENT_DISCONNECT, disconnectListener)
		.on(Socket.EVENT_ERROR, errorListener)
		.on("donation", donationListener);
	}
	
	public void Connect(String token) throws JSONException {
		_token = token;
		sock.connect();
		sock.emit("add-user", new JSONObject()
			.put("token", _token)
			.put("type", "minor"));
	}
	
	public void Disconnect() {
		sock.disconnect();
	}
	
	public boolean getConnected() {
		return sock.connected();
	}
}