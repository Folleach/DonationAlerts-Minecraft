package net.folleach.testconsole;

import net.folleach.daintegrate.DonationAlertsEntryPoint;
import net.folleach.daintegrate.DonationAlertsIntegrate;
import net.folleach.daintegrate.EventProcessor;
import net.folleach.daintegrate.configurations.SettingsDto;
import net.folleach.daintegrate.configurations.SettingsDtoTransformer;
import net.folleach.daintegrate.configurations.sources.FileConfigurationSource;
import net.folleach.daintegrate.handlers.CommandHandler;
import net.folleach.daintegrate.handlers.MessageHandler;
import net.folleach.daintegrate.listeners.DonationAlertsEventListener;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.RangeSensitive;
import net.folleach.dontaionalerts.DonationAlertsClient;
import net.folleach.dontaionalerts.DonationAlertsEvent;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
        var token = reader.readLine();

        DonationAlertsEntryPoint.Main("DonationAlerts\\src\\main\\resources\\", "settings.json");

        var listener = new DonationAlertsEventListener(new EventProcessor(), DonationAlertsIntegrate.getEventListeners());
        try {
            var client = new DonationAlertsClient("https://socket.donationalerts.ru:443", listener);
            client.connect(token);

            while (true)
                Thread.sleep(1000);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
