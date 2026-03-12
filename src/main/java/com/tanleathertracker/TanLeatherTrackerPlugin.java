package com.tanleathertracker;

import com.google.inject.Provides;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.MouseListener;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@PluginDescriptor(
	name = "Tan Leather Cast Tracker",
	description = "Tracks Tan Leather spell casts and checks inventory for dragon hides or leather",
	tags = {"magic", "crafting", "lunar", "tan", "leather", "tracker"}
)
public class TanLeatherTrackerPlugin extends Plugin implements MouseListener
{
	private static final Logger log = LoggerFactory.getLogger(TanLeatherTrackerPlugin.class);

	// Untanned dragon hides — consumed by Tan Leather spell
	private static final Set<Integer> HIDE_ITEM_IDS = Set.of(
		1753, // Green dragon hide
		1751, // Blue dragon hide
		1749, // Red dragon hide
		1747  // Black dragon hide
	);

	@Inject
	private Client client;

	@Inject
	private TanLeatherTrackerConfig config;

	@Inject
	private Notifier notifier;

	@Inject
	private MouseManager mouseManager;

	@Inject
	private TanLeatherTrackerOverlay overlay;

	@Inject
	private TanLeatherDimOverlay dimOverlay;

	@Inject
	private OverlayManager overlayManager;

	private int castCount = 0;
	private int maxCasts = 5;
	private int prevHideCount = -1;
	private boolean hasRelevantItems = false;
	private boolean lowHides = false;
	private boolean sessionActive = false;
	private boolean notifiedLowHides = false;
	private boolean dimDismissed = false;
	private boolean mouseListenerRegistered = false;

	public int getCastCount()
	{
		return castCount;
	}

	public int getMaxCasts()
	{
		return maxCasts;
	}

	public boolean hasRelevantItems()
	{
		return hasRelevantItems;
	}

	public boolean shouldShowDim()
	{
		return sessionActive
			&& lowHides
			&& !dimDismissed
			&& config.notifierEnabled()
			&& config.notifierType() == NotifierType.SCREEN_DIM;
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		overlayManager.add(dimOverlay);
		castCount = 0;
		prevHideCount = -1;
		sessionActive = false;
		notifiedLowHides = false;
		lowHides = false;
		dimDismissed = false;
		if (mouseListenerRegistered)
		{
			mouseManager.unregisterMouseListener(this);
			mouseListenerRegistered = false;
		}
		log.debug("Tan Leather Cast Tracker started");
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		overlayManager.remove(dimOverlay);
		castCount = 0;
		prevHideCount = -1;
		sessionActive = false;
		notifiedLowHides = false;
		lowHides = false;
		dimDismissed = false;
		if (mouseListenerRegistered)
		{
			mouseManager.unregisterMouseListener(this);
			mouseListenerRegistered = false;
		}
		log.debug("Tan Leather Cast Tracker stopped");
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId())
		{
			return;
		}

		int currentHideCount = countHides(event.getItemContainer());
		hasRelevantItems = currentHideCount > 0;

		boolean castDetected = prevHideCount != -1 && currentHideCount < prevHideCount;

		// Cast count logic
		if (castDetected)
		{
			if (castCount == 0)
			{
				// Lock in maxCasts from the hide count before this cast
				maxCasts = (int) Math.ceil(prevHideCount / 5.0);
			}
			castCount++;
			if (castCount >= maxCasts)
			{
				castCount = 0;
			}
			sessionActive = true;
		}

		// Update low hides flag
		lowHides = currentHideCount < 5;

		// Fire sound notifier once when hides drop below 5 after casting
		if (sessionActive && lowHides && !notifiedLowHides
			&& config.notifierEnabled()
			&& config.notifierType() == NotifierType.SOUND)
		{
			notifier.notify("Not enough dragon hides for Tan Leather!");
			notifiedLowHides = true;
		}

		// Register mouse listener only when dim first becomes active
		boolean dimShouldShow = sessionActive
			&& lowHides
			&& !dimDismissed
			&& config.notifierEnabled()
			&& config.notifierType() == NotifierType.SCREEN_DIM;

		if (dimShouldShow && !mouseListenerRegistered)
		{
			mouseManager.registerMouseListener(this);
			mouseListenerRegistered = true;
		}

		// Re-arm when restocked to 5 or more
		if (currentHideCount >= 5)
		{
			notifiedLowHides = false;
			dimDismissed = false;
		}

		prevHideCount = currentHideCount;
	}

	private int countHides(ItemContainer container)
	{
		int count = 0;
		Item[] items = container.getItems();
		if (items == null)
		{
			return 0;
		}
		for (Item item : items)
		{
			if (item != null && HIDE_ITEM_IDS.contains(item.getId()))
			{
				count += item.getQuantity();
			}
		}
		return count;
	}

	// --- MouseListener ---

	@Override
	public MouseEvent mouseMoved(MouseEvent event)
	{
		if (mouseListenerRegistered)
		{
			dimDismissed = true;
			mouseManager.unregisterMouseListener(this);
			mouseListenerRegistered = false;
		}
		return event;
	}

	@Override
	public MouseEvent mouseClicked(MouseEvent event) { return event; }

	@Override
	public MouseEvent mousePressed(MouseEvent event) { return event; }

	@Override
	public MouseEvent mouseReleased(MouseEvent event) { return event; }

	@Override
	public MouseEvent mouseEntered(MouseEvent event) { return event; }

	@Override
	public MouseEvent mouseExited(MouseEvent event) { return event; }

	@Override
	public MouseEvent mouseDragged(MouseEvent event) { return event; }

	@Provides
	TanLeatherTrackerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TanLeatherTrackerConfig.class);
	}
}
