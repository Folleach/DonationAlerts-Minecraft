package com.folleach.daintegrate;

import org.json.JSONObject;

public class Action {
    public String executor;
    public int delay;
    public JSONObject data;

    public Action() {
    }

    public Action(String executor, int delay, JSONObject data) {
        this.executor = executor;
        this.delay = delay;
        this.data = data;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("executor", executor);
        json.put("delay", delay);
        json.put("data", data);
        return json;
    }

    public static Action fromJson(JSONObject json) {
        Action instance = new Action();
        instance.executor = json.getString("executor");
        instance.delay = json.getInt("delay");
        instance.data = json.getJSONObject("data");
        return instance;
    }
}
