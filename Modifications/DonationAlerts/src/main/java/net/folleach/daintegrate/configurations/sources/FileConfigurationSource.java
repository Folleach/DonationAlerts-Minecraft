package net.folleach.daintegrate.configurations.sources;

import net.folleach.daintegrate.Constants;
import net.folleach.daintegrate.IConfigurationSource;
import net.folleach.daintegrate.ITransformer;
import net.folleach.daintegrate.SimpleRepresenter;
import net.folleach.daintegrate.configurations.*;
import net.folleach.daintegrate.handlers.MessageHandlerProperties;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.AlwaysSensitive;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.constant.Constable;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileConfigurationSource implements IConfigurationSource {
    private final String path;
    private final String watchedFileName;
    private final WatchService watcher;
    private final ArrayList<IListener<SettingsDto>> listeners;
    private final ITransformer<String, SettingsDto> transformer;
    private final IListener<String> log;
    private final MessageDigest md;
    private SettingsDto current;

    {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private Thread thread;
    private boolean pooling;
    private byte[] currentHash;

    public FileConfigurationSource(
            String path,
            String file,
            ArrayList<IListener<SettingsDto>> listeners,
            ITransformer<String, SettingsDto> transformer,
            IListener<String> log) throws IOException {
        this.transformer = transformer;
        this.log = log;
        var cp = Paths.get(path);
        createIfNotExists(path, file);
        this.path = cp.toString();
        watchedFileName = file;
        watcher = FileSystems.getDefault().newWatchService();
        cp.register(watcher, ENTRY_MODIFY);
        this.listeners = listeners;
    }

    private void createIfNotExists(String path, String file) {
        var resultMkdirs = new File(path).mkdirs();
        var fileHandle = new File(path, file);
        if (!fileHandle.exists())
        {
            try {
                var result = fileHandle.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                var alwaysProperties = new PropertiesDto();
                var handlerMessageProperties = new PropertiesDto();
                alwaysProperties.type = Constants.ModId + "/sensitive/always";
                var always = new SensitivePropertiesDto();
                always.properties = alwaysProperties;
                var trigger = new TriggerDto();

                var messageProps = new MessageHandlerProperties();
                messageProps.message = "Hello! This is an example message for all events from Donation Alerts";
                handlerMessageProperties.type = Constants.ModId + "/handler/message";
                handlerMessageProperties.value = messageProps;
                var message = new HandlerPropertiesDto();
                message.properties = handlerMessageProperties;
                trigger.sensitives = new SensitivePropertiesDto[1];
                trigger.sensitives[0] = always;
                trigger.isActive = true;
                trigger.name = "default";
                trigger.description = "example trigger, check " + Constants.GuideToConfiguration + " to learn how to set up this";
                trigger.handlers = new HandlerPropertiesDto[1];
                trigger.handlers[0] = message;

                var defaultSettings = new SettingsDto();
                defaultSettings.triggers = new TriggerDto[1];
                defaultSettings.triggers[0] = trigger;

                var options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                var representer = new Representer(options);
                representer.addClassTag(SettingsDto.class, Tag.MAP);
                representer.addClassTag(MessageHandlerProperties.class, Tag.MAP);
                var yaml = new Yaml(representer, options);
                PrintWriter writer = new PrintWriter(fileHandle, StandardCharsets.UTF_8);
                writer.print(yaml.dump(defaultSettings));
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onValue(SettingsDto value) {
        for (var listener : listeners)
            listener.onValue(value);
    }

    @Override
    public void startListening() {
        if (pooling)
            return;
        pooling = true;
        tryUpdate();
        thread = new Thread(() -> {
            while (pooling) {
                WatchKey key = null;
                try {
                    key = watcher.take();
                    for (WatchEvent<?> e : key.pollEvents()) {
                        var event = (WatchEvent<Path>)e;
                        WatchEvent.Kind<?> kind = event.kind();
                        if (kind == OVERFLOW)
                            continue;
                        Path file = event.context().getFileName();
                        if (!file.toString().equals(watchedFileName))
                            continue;
                        tryUpdate();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    if (key != null && !key.reset())
                        break;
                }
            }
        });
        thread.start();
    }

    private void tryUpdate() {
        try {
            String json = null;
            json = Files.readString(Paths.get(path, watchedFileName));
            if (json == null)
                return;
            var hash = md.digest(json.getBytes());
            if (Arrays.equals(hash, currentHash))
                return;
            currentHash = hash;
            var value = transformer.transform(json);
            if (value != null) {
                onValue(value);
                current = value;
            }
        } catch (Exception e) {
            log.onValue("error while update the settings from file: " + e);
        }
    }

    @Override
    public void stopListening() {
        try {
            watcher.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pooling = false;
    }

    public void addListener(IListener<SettingsDto> listener) {
        listeners.add(listener);
    }

    public SettingsDto getCurrent() {
        return current;
    }
}
