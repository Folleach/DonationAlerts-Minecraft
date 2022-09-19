package net.folleach.testconsole;

import com.google.gson.GsonBuilder;
import net.folleach.daintegrate.DonationAlertsIntegrate;
import net.folleach.daintegrate.EventProcessor;
import net.folleach.daintegrate.PropertyDeserializer;
import net.folleach.daintegrate.configurations.IProperties;
import net.folleach.daintegrate.configurations.SettingsDto;
import net.folleach.daintegrate.handlers.CommandHandler;
import net.folleach.daintegrate.handlers.MessageHandler;
import net.folleach.daintegrate.listeners.DonationAlertsEventListener;
import net.folleach.daintegrate.sensitives.RangeSensitive;
import net.folleach.dontaionalerts.DonationAlertsClient;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
        var token = reader.readLine();

        var json = Files.readString(Paths.get("DonationAlerts\\src\\main\\resources\\settings.json"));

        DonationAlertsIntegrate
                .configure("daintegratew", "https://folleach.ru/l/daintegrate")
                .registerHandler(new MessageHandler())
                .registerHandler(new CommandHandler())
                .registerSensitive(new RangeSensitive())
                .registerEventListener(e -> {
                    System.out.println(e.getAmount());
                });

        var gson = new GsonBuilder().registerTypeHierarchyAdapter(IProperties.class, new PropertyDeserializer()).create();
        var ins = gson.fromJson(json, SettingsDto.class);

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
