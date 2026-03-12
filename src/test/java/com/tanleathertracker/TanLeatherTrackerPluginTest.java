package com.tanleathertracker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TanLeatherTrackerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TanLeatherTrackerPlugin.class);
		RuneLite.main(args);
	}
}
