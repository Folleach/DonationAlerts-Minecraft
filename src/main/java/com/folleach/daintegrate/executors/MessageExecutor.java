package com.folleach.daintegrate.executors;

import com.folleach.daintegrate.DataCollector;
import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Requirement;
import com.folleach.donationalerts.DonationAlertsEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeHooks;
import org.json.JSONObject;

public class MessageExecutor implements IExecutorEntity {

    private Minecraft minecraft;

    public MessageExecutor(Minecraft minecraft) {
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
        return "message";
    }

    @Override
    public void Execute(DonationAlertsEvent event, JSONObject data) {
        String message = data.getString("message");
        message = DonationAlertsIntegrate.ReplaceConstants(message, event);
        minecraft.ingameGUI.getChatGUI().printChatMessage(ForgeHooks.newChatWithLinks(message));
    }

    public IExecutorEntity parse(JSONObject json) {
        return null;
    }
}
