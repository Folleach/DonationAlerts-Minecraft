package net.folleach.daintegrate;

import net.fabricmc.api.ModInitializer;
import net.folleach.dontaionalerts.DonationAlertsCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		var instance = new DonationAlertsCore();
		instance.print();
	}
}
