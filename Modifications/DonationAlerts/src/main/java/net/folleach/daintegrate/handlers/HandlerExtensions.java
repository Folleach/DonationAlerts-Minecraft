package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;

public class HandlerExtensions {
    public static String getUniqueHandlerId(IHandler handler) {
        return handler.getModId() + "/" + handler.getHandlerId();
    }
}
