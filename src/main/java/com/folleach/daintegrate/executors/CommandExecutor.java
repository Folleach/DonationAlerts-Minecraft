package com.folleach.daintegrate.executors;

import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Requirement;
import com.folleach.donationalerts.DonationAlertsEvent;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;

public class CommandExecutor implements IExecutorEntity {

    private Minecraft minecraft;

    public CommandExecutor(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public Requirement[] getRequires() {
        return new Requirement[] {
                new Requirement("command", Requirement.RequirementType.String)
        };
    }

    @Override
    public String getExecutorName() {
        return "command";
    }

    @Override
    public void Execute(DonationAlertsEvent event, JSONObject data) {
        String command = data.getString("command");
        command = DonationAlertsIntegrate.ReplaceConstants(command, event);
        minecraft.player.sendChatMessage(command);
    }
}
