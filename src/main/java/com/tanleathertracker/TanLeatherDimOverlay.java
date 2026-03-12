package com.tanleathertracker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class TanLeatherDimOverlay extends Overlay
{
	private static final Color DIM_COLOR = new Color(0, 0, 0, 180);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final String MESSAGE = "Out of dragon hides!";

	private final Client client;
	private final TanLeatherTrackerPlugin plugin;

	@Inject
	public TanLeatherDimOverlay(Client client, TanLeatherTrackerPlugin plugin)
	{
		this.client = client;
		this.plugin = plugin;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.shouldShowDim())
		{
			return null;
		}

		Dimension size = client.getRealDimensions();

		// Darken the screen
		graphics.setColor(DIM_COLOR);
		graphics.fillRect(0, 0, size.width, size.height);

		// Draw centred message
		graphics.setColor(TEXT_COLOR);
		graphics.setFont(new Font("Arial", Font.BOLD, 24));
		FontMetrics fm = graphics.getFontMetrics();
		int x = (size.width - fm.stringWidth(MESSAGE)) / 2;
		int y = (size.height / 2) + fm.getAscent() / 2;
		graphics.drawString(MESSAGE, x, y);

		return size;
	}
}
