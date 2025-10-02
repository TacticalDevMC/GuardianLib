package net.rubixdevelopment.guardianlibApi.gui;
/*
 Created and owned by TacticalDev
 Created on 30/11/2024 at time 22:43
*/

import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PagedGUI extends GUI {

    private final List<Button> items;
    private int currentPage;
    private final int itemsPerPage;
    private Plugin plugin;

    public PagedGUI(Plugin plugin, String title, int rows) {
        super(plugin, title, rows);
        this.plugin = plugin;
        this.items = new ArrayList<>();
        this.itemsPerPage = (rows - 1) * 9;
    }

    public void addItem(Button button) {
        items.add(button);
    }

    public void showPage(int page) {
        currentPage = Math.max(0, Math.min(page, items.size() / (getInventory().getSize() * 9)));
        clearItems();

        int start = currentPage * itemsPerPage;

        fillEmptySlotsWithGlass();

        new BukkitRunnable() {
            int i = start;
            int index = 0;

            @Override
            public void run() {
                if (i >= start + itemsPerPage || i >= items.size()) {
                    cancel();
                    return;
                }

                Button original = items.get(i);
                addButton(index, original);

                i++;
                index++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    /**
     * Wist alleen de knoppen die worden gebruikt voor paginated content.
     */
    public void clearItems() {
        for (int i = 0; i < itemsPerPage; i++) {
            removeButton(i);
        }
    }

    public int getMaxPages() {
        return (int) Math.ceil(items.size() / (items.size() / (double) itemsPerPage));
    }

    /**
     * Geeft het laatste slot (bijv. 26 bij 3 rijen).
     */
    public int getLastSlot() {
        return getInventory().getSize() - 1;
    }

    /**
     * Geeft het eerste slot op de onderste rij.
     */
    public int getBottomRowStart() {
        return getInventory().getSize() - 9;
    }

    /**
     * Geeft het slot in het midden van de onderste rij.
     */
    public int getBottomCenterSlot() {
        return getBottomRowStart() + 4;
    }

    /**
     * Zet een vaste layout template voor knoppen. Je geeft een mapping van slot → item.
     */
    public void applyLayout(Function<Integer, Button> layoutFunction, int... slots) {
        for (int slot : slots) {
            Button button = layoutFunction.apply(slot);
            if (button != null) {
                addButton(slot, button);
            }
        }
    }
}