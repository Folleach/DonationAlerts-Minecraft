package net.folleach.dontaionalerts;

public enum  AlertType {
    Undefined(-1),
    Donate(1),
    TwitchSubscription(4),
    TwitchFreeFollow(6),
    YouTubeSubscription(7);

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
            default:
                return AlertType.Undefined;
        }
    }
}
