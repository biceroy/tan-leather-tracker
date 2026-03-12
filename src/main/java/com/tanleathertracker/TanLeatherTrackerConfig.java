package com.tanleathertracker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tanleathertracker")
public interface TanLeatherTrackerConfig extends Config
{
	@ConfigItem(
		keyName = "showOverlay",
		name = "Show Overlay",
		description = "Show the cast count overlay when dragon hides are in inventory"
	)
	default boolean showOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "notifierEnabled",
		name = "Enable Low Hides Notifier",
		description = "Send a notification when dragon hides drop below 5 after casting"
	)
	default boolean notifierEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "notifierType",
		name = "Notifier Type",
		description = "Sound/tray notification or screen dim overlay when hides run out"
	)
	default NotifierType notifierType()
	{
		return NotifierType.SOUND;
	}
}
