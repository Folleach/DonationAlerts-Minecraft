package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.SettingsDto;
import net.folleach.daintegrate.configurations.SettingsDtoTransformer;
import net.folleach.daintegrate.configurations.sources.FileConfigurationSource;
import net.folleach.daintegrate.handlers.CommandHandler;
import net.folleach.daintegrate.handlers.MessageHandler;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.AlwaysSensitive;
import net.folleach.daintegrate.sensitives.RangeSensitive;

import java.io.IOException;
import java.util.ArrayList;

public class DonationAlertsEntryPoint {
    public static void Main(String settingsPath, String settingsFile) throws IOException {
        var instance = DonationAlertsIntegrate.Instance;

        DonationAlertsIntegrate
                .configure(Constants.ModId, "https://folleach.ru/l/daintegrate")
                .registerHandler(new MessageHandler())
                .registerHandler(new CommandHandler())
                .registerSensitive(new RangeSensitive())
                .registerSensitive(new AlwaysSensitive())
                .registerEventListener(event -> System.out.println("Receive new event"));

        var configurationListeners = new ArrayList<IListener<SettingsDto>>();
        configurationListeners.add(configuration -> instance.updateTriggers(configuration.triggers));
        var configurationSource = new FileConfigurationSource(
                settingsPath,
                settingsFile,
                configurationListeners,
                new SettingsDtoTransformer()
        );
        configurationSource.startListening();
    }
}
