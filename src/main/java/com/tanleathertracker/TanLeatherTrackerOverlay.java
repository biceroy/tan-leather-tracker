package com.tanleathertracker;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

public class TanLeatherTrackerOverlay extends OverlayPanel
{
	private final TanLeatherTrackerPlugin plugin;
	private final TanLeatherTrackerConfig config;

	@Inject
	public TanLeatherTrackerOverlay(TanLeatherTrackerPlugin plugin, TanLeatherTrackerConfig config)
	{
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.TOP_LEFT);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showOverlay() || !plugin.isHasRelevantItems())
		{
			return null;
		}

		int castCount = plugin.getCastCount();
		int maxCasts = plugin.getMaxCasts();
		String displayText = castCount + " / " + maxCasts;

		Color textColor = (castCount >= maxCasts && maxCasts > 0) ? Color.RED : Color.WHITE;

		panelComponent.getChildren().add(LineComponent.builder()
			.left("Tan Casts:")
			.right(displayText)
			.rightColor(textColor)
			.build());

		return super.render(graphics);
	}
}
