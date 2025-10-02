package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:23
*/

import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface GuiFactory {
    GUI gui(String title, int rows);
    interface GUI {
        void addButton(int slot, ItemStack icon, Consumer<Click> onClick);
        void setFillGlass(boolean enabled);
        void setCloseOnClick(int slot, boolean close);
        void open(org.bukkit.entity.Player player);
        interface Click {
            org.bukkit.entity.Player player();
            org.bukkit.event.inventory.ClickType clickType();
            void close();
        }
    }
}
