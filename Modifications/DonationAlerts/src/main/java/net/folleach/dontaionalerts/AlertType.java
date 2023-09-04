package net.folleach.dontaionalerts;

import com.google.gson.annotations.SerializedName;

public enum AlertType {
    @SerializedName("-1")
    Undefined(-1),
    @SerializedName("1")
    Donate(1),
    @SerializedName("4")
    TwitchSubscription(4),
    @SerializedName("6")
    TwitchFreeFollow(6),
    @SerializedName("7")
    YouTubeSubscription(7),
    @SerializedName("11")
    TwitchBits(11),
    @SerializedName("13")
    TwitchGiftSubscription(13),
    @SerializedName("15")
    TwitchPrimeSubscription(15),
    @SerializedName("19")
    TwitchPoints(19);

    public final int Value;

    AlertType(int value) {
        Value = value;
    }

    public static AlertType valueOf(int value) {
        switch (value) {
            case 1:
                return AlertType.Donate;
            case 4:
                return AlertType.TwitchSubscription;
            case 6:
                return AlertType.TwitchFreeFollow;
            case 7:
                return AlertType.YouTubeSubscription;
            case 11:
                return AlertType.TwitchBits;
            case 13:
                return AlertType.TwitchGiftSubscription;
            case 15:
                return AlertType.TwitchPrimeSubscription;
            default:
                return AlertType.Undefined;
        }
    }
}
