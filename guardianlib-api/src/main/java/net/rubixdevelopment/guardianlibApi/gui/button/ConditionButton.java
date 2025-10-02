package net.rubixdevelopment.guardianlibApi.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ConditionButton extends Button {

    private final Predicate<Player> condition;

    public ConditionButton(ItemStack icon, Predicate<Player> condition, Consumer<Player> onClick) {
        super(icon, onClick);
        this.condition = condition;
    }

    public boolean isVisible(Player player) {
        return condition.test(player);
    }
}