package net.folleach.dontaionalerts;

import java.util.Date;

public class DonationAlertsEvent {
    public int ID;
    public AlertType Type;
    public String IsShown;
    public AdditionalData Additional;
    public String BillingSystem;
    public String BillingSystemType;
    public String UserName;
    public float Amount;
    public String AmountFormatted;
    public float AmountMain;
    public String Currency;
    public String Message;
    public Date DateCreated;
    public String Emotes;
    public String ApId;
    public boolean IsTest;
}
