package net.folleach.daintegrate.listeners;

public interface IListener<T> {
    void onValue(T value);
}
