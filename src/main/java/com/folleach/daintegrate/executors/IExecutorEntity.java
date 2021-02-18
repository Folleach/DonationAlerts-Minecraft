package com.folleach.daintegrate.executors;

import com.folleach.donationalerts.DonationAlertsEvent;
import org.json.JSONObject;

public interface IExecutorEntity {
    String getExecutorName();
    void Execute(DonationAlertsEvent event, JSONObject data);
}
