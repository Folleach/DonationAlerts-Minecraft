package com.folleach.daintegrate;

import com.folleach.daintegrate.executors.IExecutorEntity;
import com.folleach.donationalerts.AlertType;
import com.folleach.donationalerts.DonationAlertsEvent;
import com.folleach.donationalerts.DonationType;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeHooks;

import java.util.HashMap;
import java.util.List;

public class DonationAlertsIntegrate {
    public static final String TagDonationMessage      = "<donation_message>";
    public static final String TagDonationAmount       = "<donation_amount>";
    public static final String TagDonationCurrency     = "<donation_currency>";
    public static final String TagDonationUserName     = "<donation_username>";
    public static final String TagMinecraftPlayerName  = "<minecraft_playername>";

    public List<DonationAlertsEvent> Donations;

    private HashMap<String, IExecutorEntity> executors = new HashMap<>();
    private DataCollector data;

    public DonationAlertsIntegrate(Minecraft minecraft, DataCollector data) {
        this.data = data;
        Donations = Lists.<DonationAlertsEvent>newArrayList();
    }

    public void addExecutor(IExecutorEntity executor) {
        executors.put(executor.getExecutorName(), executor);
    }

    public void addEvent(DonationAlertsEvent event) {
        if (event.Type == AlertType.Donate)
            handle(event);
        else
            Main.DonationAlertsInformation("Invalid type: " + event.Type.toString());
    }

    public void RecountDonationCache()
    {
        while (Donations.size() > data.CountDonationInCache)
            Donations.remove(0);
    }

    public static String ReplaceConstants(String pattern, DonationAlertsEvent event) {
        pattern = pattern.replace(TagDonationMessage, event.Message);
        pattern = pattern.replace(TagDonationAmount, String.valueOf(event.Amount));
        pattern = pattern.replace(TagDonationCurrency, String.valueOf(event.Currency));
        pattern = pattern.replace(TagDonationUserName, String.valueOf(event.UserName));
        pattern = pattern.replace(TagMinecraftPlayerName, Minecraft.getInstance().player.getName().getString());
        return pattern;
    }

    private void handle(DonationAlertsEvent event) {
        if (data.SkipTestDonation && event.IsTest)
            return;
        Donations.add(event);
        DonationType donationType = findDonationType(event);
        if (donationType == null)
            return;

        List<Action> actions = donationType.getActions();
        for (Action action : actions) {
            IExecutorEntity executor = executors.get(action.executor);
            executor.Execute(event, action.data);
        }
        RecountDonationCache();
    }

    private DonationType findDonationType(DonationAlertsEvent event) {
        DonationType donationType = null;
        for (int i = 0; i < data.typesManager.getTypes().size(); i++)
        {
            if (event.Amount >= data.typesManager.get(i).getAmountByCurrency(event.Currency)
                    && (donationType == null || data.typesManager.get(i).getAmountByCurrency(event.Currency) > donationType.getAmountByCurrency(event.Currency))
                    && data.typesManager.get(i).Active) {
                donationType = data.typesManager.get(i);
            }
        }
        return donationType;
    }
}
