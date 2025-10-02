package net.rubixdevelopment.guardianlibPlugin.impl;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 18:38
*/

import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import net.rubixdevelopment.guardianlibApi.gui.button.functions.ClickContext;
import net.rubixdevelopment.guardianlibApi.interfaces.GuiFactory;
import net.rubixdevelopment.guardianlibApi.interfaces.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GuiFactoryImpl implements GuiFactory, Listener {
    private final Plugin plugin;
    private final Text text;

    public GuiFactoryImpl(Plugin plugin) {
        this.plugin = plugin;
        this.text = GuardianLibProvider.get().text();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public GUI gui(String title, int rows) {
        // Adapter die zowel jouw bestaande GUI als het GuiFactory.GUI interface implementeert
        return new GuardianCoreBackedGUI(plugin, text.color(title), clampRows(rows));
    }

    private int clampRows(int rows) {
        if (rows < 1) return 1;
        if (rows > 6) return 6;
        return rows;
    }

    /**
     * Adapter: gebruikt de bestaande GuardianCore GUI, en expose't het GuiFactory.GUI interface.
     */
    private final class GuardianCoreBackedGUI extends net.rubixdevelopment.guardianlibApi.gui.GUI
            implements GuiFactory.GUI, Listener {

        private final Map<Integer, Consumer<Click>> handlers = new HashMap<>();
        private final Map<Integer, Boolean> closeOnClick = new HashMap<>();
        private boolean fillGlass = false;

        GuardianCoreBackedGUI(Plugin plugin, String title, int rows) {
            super(plugin, title, rows);
            // Deze GUI luistert al naar clicks via GUI#handleClick (Listener),
            // maar we willen eigen onClick-gedrag koppelen. Dat doen we door
            // Buttons met ClickHandlers te registreren (zodat ClickType mee komt).
        }

        // GuiFactory.GUI API ----------

        @Override
        public void addButton(int slot, ItemStack icon, Consumer<Click> onClick) {
            // Maak een Button die via ClickContext (met ClickType) onze Consumer<Click> aanroept
            Button b = new Button(icon, /* sound = */ null, (ClickContext ctx) -> {
                Player p = ctx.getPlayer();
                Click click = new Click() {
                    @Override public Player player() { return p; }
                    @Override public org.bukkit.event.inventory.ClickType clickType() { return ctx.getClick(); }
                    @Override public void close() { p.closeInventory(); }
                };
                onClick.accept(click);

                if (Boolean.TRUE.equals(closeOnClick.get(slot))) {
                    p.closeInventory();
                }
            });
            // Belangrijk: bovenstaande ctor zet useSounds=true, maar sound is null.
            // In Button wordt dan niets afgespeeld (isUseSounds && sound != null).
            // Alternatief was: b.addClickHandler(...), maar dit is compact.

            super.addButton(slot, b);
            handlers.put(slot, onClick);
        }

        @Override
        public void setFillGlass(boolean enabled) { this.fillGlass = enabled; }

        @Override
        public void setCloseOnClick(int slot, boolean close) { closeOnClick.put(slot, close); }

        @Override
        public void open(Player player) {
            if (fillGlass) {
                // Gebruik jouw eigen helper
                super.fillEmptySlotsWithGlass();
            }
            super.openFor(player);
        }
    }

    // Deze listener is niet strikt nodig voor de adapter (de GuardianCore GUI handelt clicks zelf af),
    // maar je had 'm in de oorspronkelijke versie, dus we laten 'm staan voor eventuele globale hooks.
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onAnyClick(InventoryClickEvent e) {
        // Globale observatiepunt indien je ooit analytics/logging wilt doen
    }
}
