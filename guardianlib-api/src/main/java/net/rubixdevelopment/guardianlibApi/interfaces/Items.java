package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:33
*/

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public interface Items {
    ItemBuilder builder(Material mat);

    // NIEUW (optioneel maar handig):
    default ItemBuilder builder(ItemStack base) {
        return builder(base.getType()).from(base);
    }

    interface ItemBuilder {
        ItemBuilder name(String legacy);
        ItemBuilder loreLegacy(java.util.List<String> legacy);
        ItemBuilder lore(Component line);
        ItemBuilder lore(Component... lines);
        ItemBuilder lore(Iterable<Component> lines);
        ItemBuilder enchants(java.util.Map<org.bukkit.enchantments.Enchantment,Integer> ench);
        ItemBuilder transformMeta(Consumer<ItemMeta> meta);
        ItemStack build();

        // NIEUW (noodzakelijk voor adaptatie):
        default ItemBuilder from(ItemStack base) { return this; } // default no-op
    }
}
