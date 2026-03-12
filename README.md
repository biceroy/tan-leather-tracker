# Tan Leather Cast Tracker

A [RuneLite](https://runelite.net/) external plugin for Old School RuneScape that tracks your **Tan Leather** lunar spell casts per inventory cycle and warns you when you're running low on dragon hides.

---

## What It Does

When you cast **Tan Leather** (Lunar Spellbook), each cast tans up to 5 dragon hides at once. This plugin:

- **Counts your casts** for the current inventory cycle and shows a `current / max` display
- **Calculates the correct maximum** automatically — if you have 23 hides, the max is 5 casts; if you have 7 hides, it's 2 casts
- **Resets the counter** after each full cycle so it's always accurate for the current inventory
- **Hides itself** when no dragon hides are in your inventory
- **Warns you** when hides drop below 5 (i.e., not enough for a full cast), so you know to restock

Supported hides:
- Green dragon hide
- Blue dragon hide
- Red dragon hide
- Black dragon hide

---

## The Overlay

When dragon hides are detected in your inventory, a small overlay appears showing:

```
Tan Casts:   2 / 5
```

- **White** — casts remaining in the current cycle
- **Red** — you've reached the maximum (cycle complete, the counter resets on the next cast)

The overlay disappears automatically when you have no hides in your inventory.

---

## Low Hides Warning

Once a session starts (at least one cast detected) and your hide count drops below 5, the plugin fires a warning. There are two notification modes selectable in the plugin settings:

### Sound / Tray Notification
A system tray notification pops up with the message:

> *Not enough dragon hides for Tan Leather!*

The notification re-arms itself when you restock to 5 or more hides, so it will warn you again next time you run low.

> **Note:** Tray notifications require RuneLite's built-in notification setting to be enabled.
> Go to **RuneLite Settings → Notifications** and make sure **"Send notifications"** is turned on.
> Also check that Windows Focus Assist / Do Not Disturb is not blocking notifications.

### Screen Dim
A dark overlay covers the game screen with a centred message:

> *Out of dragon hides!*

Move your mouse to dismiss the dim. It will reappear if you restock and run low again.

---

## Settings

| Setting | Description | Default |
|---------|-------------|---------|
| **Show Overlay** | Show the cast count overlay when dragon hides are in your inventory | Enabled |
| **Enable Low Hides Notifier** | Fire a warning when hides drop below 5 after casting | Enabled |
| **Notifier Type** | Choose between **Sound** (tray notification) or **Screen Dim** (dark overlay) | Sound |

---

## How the Cast Cycle Works

1. You start with some hides in your inventory (e.g. 27)
2. On your **first cast**, the plugin locks in `maxCasts = ceil(27 / 5) = 6`
3. Each detected cast increments the counter: `1 / 6`, `2 / 6`, …
4. When the counter reaches `6 / 6`, it resets to `0` automatically
5. On your next inventory with fresh hides, the whole process repeats

The plugin uses **ItemContainerChanged** (a server-confirmed inventory update) to detect casts — not click events — so spam-clicking the spell never inflates the count.

---

## Installation (Developer / Testing)

This plugin is not yet on the Plugin Hub. To run it locally:

1. Clone this repository
2. Open the project in IntelliJ IDEA with a Java 11+ JDK configured
3. Run the main method in `src/test/java/com/tanleathertracker/TanLeatherTrackerPluginTest.java`
4. Log in via the RuneLite launcher that opens

---

## Plugin Hub Submission

This plugin is intended for submission to the [RuneLite Plugin Hub](https://github.com/runelite/plugin-hub).
See the Plugin Hub README for submission requirements.

---

## License

[BSD 2-Clause License](LICENSE)
