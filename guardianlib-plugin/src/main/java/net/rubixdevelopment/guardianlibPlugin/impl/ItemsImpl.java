package net.rubixdevelopment.guardianlibPlugin.impl;

import net.kyori.adventure.text.Component;
import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.interfaces.Items;
import net.rubixdevelopment.guardianlibApi.interfaces.Text;
import net.rubixdevelopment.guardianlibPlugin.impl.builder.ProvidedItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Adapter die de rijke ProvidedItemBuilder onder water gebruikt,
 * maar de API van Items.ItemBuilder respecteert.
 */
public class ItemsImpl implements Items {
    private final Text text = GuardianLibProvider.get().text();

    @Override
    public ItemBuilder builder(Material mat) {
        return new Adapter(new ProvidedItemBuilder(mat));
    }

    // Optioneel – alleen als je de API-aanvulling toevoegde:
    @Override
    public ItemBuilder builder(ItemStack base) {
        return new Adapter(new ProvidedItemBuilder(base));
    }

    private final class Adapter implements ItemBuilder {
        private final ProvidedItemBuilder delegate;

        private Adapter(ProvidedItemBuilder d) { this.delegate = d; }

        @Override
        public ItemBuilder name(String legacy) {
            delegate.name(legacy);
            return this;
        }

        @Override
        public ItemBuilder loreLegacy(List<String> legacy) {
            delegate.loreLegacy(legacy);
            return this;
        }

        @Override
        public ItemBuilder lore(Component... lines) {
            delegate.lore(lines);
            return this;
        }

        @Override
        public ItemBuilder lore(Component line) {
            delegate.lore(line);
            return this;
        }

        @Override
        public ItemBuilder lore(Iterable<Component> lines) {
            delegate.lore(lines);
            return this;
        }

        @Override
        public ItemBuilder enchants(Map<Enchantment, Integer> ench) {
            delegate.enchants(ench);
            return this;
        }

        // Extra handige shortcuts die niet in de API zaten – optioneel:
        public ItemBuilder amount(int amount) { delegate.amount(amount); return this; }
        public ItemBuilder glow(boolean glow) {
            if (glow) {
                delegate.enchant(Enchantment.LURE, 1).flag(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            }
            return this;
        }
        public ItemBuilder meta(java.util.function.Consumer<ItemMeta> c) {
            delegate.changeMeta(c);
            return this;
        }

        @Override
        public ItemStack build() { return delegate.build(); }

        // Optioneel (API-aanvulling)
        @Override
        public ItemBuilder from(ItemStack base) {
            // Replace internal stack by copying properties of base
            delegate.type(base.getType()).amount(base.getAmount()).changeMeta(m -> {
                ItemMeta bm = base.getItemMeta();
                if (bm == null) return;
                m.displayName(bm.displayName());
                m.lore(bm.lore());
                bm.getEnchants().forEach((e,l)-> m.addEnchant(e,l,true));
                m.setCustomModelData(bm.hasCustomModelData()? bm.getCustomModelData() : null);
                m.addItemFlags(bm.getItemFlags().toArray(org.bukkit.inventory.ItemFlag[]::new));
            });
            return this;
        }

        @Override
        public ItemBuilder transformMeta(final Consumer<ItemMeta> meta) {
            delegate.transformMeta(meta);
            return this;
        }
    }
}