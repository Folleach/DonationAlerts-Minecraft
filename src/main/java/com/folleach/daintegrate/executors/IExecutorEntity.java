package com.folleach.daintegrate.executors;

import com.folleach.daintegrate.Requirement;
import com.folleach.donationalerts.DonationAlertsEvent;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;

public interface IExecutorEntity {
    Requirement[] getRequires();
    String getExecutorName();
    void Execute(DonationAlertsEvent event, JSONObject data);
}
