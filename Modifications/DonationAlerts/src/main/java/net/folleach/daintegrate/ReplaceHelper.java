package net.folleach.daintegrate;

import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class ReplaceHelper {
    public static final String TagDonationMessage      = "<donation_message>";
    public static final String TagDonationAmount       = "<donation_amount>";
    public static final String TagDonationCurrency     = "<donation_currency>";
    public static final String TagDonationUserName     = "<donation_username>";
    public static final String TagMinecraftPlayerName  = "<minecraft_playername>";

    public static String replace(String pattern, ReadOnlyDonationAlertsEvent event, String playerName) {
        pattern = pattern.replace(TagDonationMessage, event.getMessage());
        pattern = pattern.replace(TagDonationAmount, String.valueOf(event.getAmount()));
        pattern = pattern.replace(TagDonationCurrency, String.valueOf(event.getCurrency()));
        pattern = pattern.replace(TagDonationUserName, String.valueOf(event.getUserName()));
        pattern = pattern.replace(TagMinecraftPlayerName, playerName);
        return pattern;
    }
}
