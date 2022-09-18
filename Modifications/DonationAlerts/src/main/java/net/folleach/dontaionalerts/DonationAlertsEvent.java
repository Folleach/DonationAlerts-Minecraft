package net.folleach.dontaionalerts;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DonationAlertsEvent {
    @SerializedName("id")
    public int ID;
    @SerializedName("alert_type")
    public AlertType Type;
    @SerializedName("is_shown")
    public String IsShown;
    @SerializedName("additional_data")
    public String Additional;
    @SerializedName("billing_system")
    public String BillingSystem;
    @SerializedName("billing_system_type")
    public String BillingSystemType;
    @SerializedName("username")
    public String UserName;
    @SerializedName("amount")
    public float Amount;
    @SerializedName("amount_formatted")
    public String AmountFormatted;
    @SerializedName("amount_main")
    public float AmountMain;
    @SerializedName("currency")
    public String Currency;
    @SerializedName("message")
    public String Message;
    @SerializedName("date_created")
    public Date DateCreated;
    @SerializedName("emotes")
    public String Emotes;
    @SerializedName("ap_id")
    public String ApId;
    @SerializedName("_is_test_alert")
    public boolean IsTest;
}
