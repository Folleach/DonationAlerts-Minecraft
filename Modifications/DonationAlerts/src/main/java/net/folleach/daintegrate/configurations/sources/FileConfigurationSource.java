package net.folleach.daintegrate.configurations.sources;

import net.folleach.daintegrate.IConfigurationSource;
import net.folleach.daintegrate.ITransformer;
import net.folleach.daintegrate.configurations.SettingsDto;
import net.folleach.daintegrate.listeners.IListener;

import java.io.IOException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileConfigurationSource implements IConfigurationSource {
    private final String path;
    private final String watchedFileName;
    private final WatchService watcher;
    private final ArrayList<IListener<SettingsDto>> listeners;
    private final ITransformer<String, SettingsDto> transformer;
    private final MessageDigest md;

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
            ITransformer<String, SettingsDto> transformer) throws IOException {
        this.transformer = transformer;
        var cp = Paths.get(path);
        this.path = cp.toString();
        watchedFileName = file;
        watcher = FileSystems.getDefault().newWatchService();
        cp.register(watcher, ENTRY_MODIFY);
        this.listeners = listeners;
        tryUpdate();
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
        String json = null;
        try {
            json = Files.readString(Paths.get(path, watchedFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (json == null)
            return;
        var hash = md.digest(json.getBytes());
        if (Arrays.equals(hash, currentHash))
            return;
        currentHash = hash;
        var value = transformer.transform(json);
        if (value != null)
            onValue(value);
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
}
