package net.rubixdevelopment.guardianlibApi.gui;

import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import net.rubixdevelopment.guardianlibApi.gui.button.ConditionButton;
import net.rubixdevelopment.guardianlibApi.gui.button.functions.ClickContext;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class GuardianGui {
    private GuardianGui() {}

    /** Maak een GUI met gekleurd titel en gecapte rows (1..6). */
    public static GUI create(Plugin plugin, String title, int rows) {
        rows = Math.max(1, Math.min(6, rows));
        return new GUI(plugin, GuardianLibProvider.get().text().color(title), rows);
    }

    /** Voeg een standaard knop toe. */
    public static void addButton(GUI gui, int slot, ItemStack icon, Consumer<Player> onClick) {
        gui.addButton(slot, new Button(icon, onClick));
    }

    /** Voeg een knop toe die na klik het menu sluit. */
    public static void addButtonClose(GUI gui, int slot, ItemStack icon, Consumer<Player> onClick) {
        gui.addButton(slot, new Button(icon, p -> {
            onClick.accept(p);
            p.closeInventory();
        }));
    }

    /** Voeg een knop toe met ClickType (via ClickContext-handlers). */
    public static void addButtonWithClickType(GUI gui, int slot, ItemStack icon, Consumer<ClickContext> handler) {
        gui.addButton(slot, new Button(icon, null, handler));
    }

    /** Voeg een ConditionButton toe (zichtbaarheid per speler). */
    public static void addConditionButton(GUI gui, int slot, ItemStack icon, Predicate<Player> condition, Consumer<Player> onClick) {
        gui.addButton(slot, new ConditionButton(icon, condition, onClick));
    }

    /** Open met optioneel automatisch glas vullen. */
    public static void open(GUI gui, Player player, boolean fillWithGlass) {
        if (fillWithGlass) {
            gui.fillEmptySlotsWithGlass();
        }
        gui.openFor(player);
    }

    /** Snel grijs glas-item. */
    public static ItemStack glass() {
        return GuardianLibProvider.get().items().builder(Material.GRAY_STAINED_GLASS_PANE)
                .transformMeta(meta -> meta.setHideTooltip(true))
                .build();
    }
}