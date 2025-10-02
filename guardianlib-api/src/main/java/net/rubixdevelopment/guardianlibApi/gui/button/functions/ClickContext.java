package net.rubixdevelopment.guardianlibApi.gui.button.functions;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ClickContext {

    private Player player;
    private ClickType click;

    public ClickContext(Player player) {
        this.player = player;
    }

    public ClickContext(ClickType click) {
        this.click = click;
    }

    public ClickContext(Player player, ClickType click) {
        this.player = player;
        this.click = click;
    }

    public Player getPlayer() {
        return player;
    }

    public ClickType getClick() {
        return click;
    }
}