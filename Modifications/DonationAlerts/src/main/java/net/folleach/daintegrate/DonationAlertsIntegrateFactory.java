package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.SettingsDto;
import net.folleach.daintegrate.configurations.SettingsDtoTransformer;
import net.folleach.daintegrate.configurations.sources.FileConfigurationSource;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.AlwaysSensitive;
import net.folleach.daintegrate.sensitives.RangeSensitive;

import java.io.IOException;
import java.util.ArrayList;

public class DonationAlertsIntegrateFactory {
    public static FileConfigurationSource create(String settingsPath, String settingsFile, IListener<String> log) throws IOException {
        log.onValue("Initialize");
        var instance = DonationAlertsIntegrate.Instance;

        DonationAlertsIntegrate
                .configure(Constants.ModId, Constants.ModUrl)
//                .registerHandler(new MessageHandler())
//                .registerHandler(new CommandHandler())
                .registerSensitive(new RangeSensitive())
                .registerSensitive(new AlwaysSensitive())
                .registerEventListener(event -> log.onValue("received new event"));

        var configurationListeners = new ArrayList<IListener<SettingsDto>>();
        configurationListeners.add(configuration -> instance.updateTriggers(configuration.triggers));
        configurationListeners.add(configuration -> log.onValue("update config with " + (configuration.triggers == null ? 0 : configuration.triggers.length) + " triggers"));
        var configurationSource = new FileConfigurationSource(
                settingsPath,
                settingsFile,
                configurationListeners,
                new SettingsDtoTransformer(),
                log
        );
        return configurationSource;
    }
}
