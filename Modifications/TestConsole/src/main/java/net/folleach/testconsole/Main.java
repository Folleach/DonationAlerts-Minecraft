package net.folleach.testconsole;

import net.folleach.dontaionalerts.DonationAlertsClient;

import java.io.*;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        BufferedReader reader = new BufferedReader(new FileReader("token.txt"));
        var token = reader.readLine();

        try {
            var client = new DonationAlertsClient("https://socket.donationalerts.ru:443");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
