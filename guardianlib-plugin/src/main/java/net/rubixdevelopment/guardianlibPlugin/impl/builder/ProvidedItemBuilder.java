package net.rubixdevelopment.guardianlibPlugin.impl.builder;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 19:16
*/

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProvidedItemBuilder {

    public static final ItemFlag[] ALL_FLAGS = new ItemFlag[]{
            ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
            ItemFlag.HIDE_DYE, ItemFlag.HIDE_ARMOR_TRIM, ItemFlag.HIDE_STORED_ENCHANTS
    };

    private static final LegacyComponentSerializer LEG = LegacyComponentSerializer.legacyAmpersand();

    private final ItemStack itemStack;

    public ProvidedItemBuilder(final Material material) {
        this.itemStack = new ItemStack(Objects.requireNonNull(material, "Material cannot be null"));
    }

    public ProvidedItemBuilder(final ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack, "ItemStack cannot be null").clone();
    }

    public ProvidedItemBuilder transform(@NotNull final Consumer<ItemStack> edit) {
        edit.accept(this.itemStack);
        return this;
    }

    public ProvidedItemBuilder transformMeta(final Consumer<ItemMeta> meta) {
        final ItemMeta m = this.itemStack.getItemMeta();
        if (m != null) {
            meta.accept(m);
            this.itemStack.setItemMeta(m);
        }
        return this;
    }

    public ProvidedItemBuilder name(final String legacy) {
        return this.transformMeta(meta -> meta.displayName(LEG.deserialize(legacy)));
    }

    public ProvidedItemBuilder name(final Component displayName) {
        return this.transformMeta(meta -> meta.displayName(displayName));
    }

    public ProvidedItemBuilder type(final Material material) {
        return this.transform(is -> is.setType(material));
    }

    public ProvidedItemBuilder lore(final Component line) {
        return this.transformMeta(meta -> {
            final List<Component> lore = meta.lore() == null ? new ArrayList<>() : new ArrayList<>(meta.lore());
            lore.add(line);
            meta.lore(lore);
        });
    }

    public ProvidedItemBuilder lore(final Component... lines) {
        return this.transformMeta(meta -> {
            final List<Component> lore = meta.lore() == null ? new ArrayList<>() : new ArrayList<>(meta.lore());
            lore.addAll(Arrays.asList(lines));
            meta.lore(lore);
        });
    }

    public ProvidedItemBuilder lore(final Iterable<Component> lines) {
        return this.transformMeta(meta -> {
            final List<Component> lore = meta.lore() == null ? new ArrayList<>() : new ArrayList<>(meta.lore());
            for (final Component line : lines) lore.add(line);
            meta.lore(lore);
        });
    }

    public ProvidedItemBuilder loreLegacy(final List<String> legacy) {
        return this.transformMeta(meta -> {
            final List<Component> lore = legacy.stream().map(LEG::deserialize).collect(Collectors.toList());
            meta.lore(lore);
        });
    }

    public ProvidedItemBuilder clearLore() {
        return this.transformMeta(meta -> meta.lore(new ArrayList<>()));
    }

    @Deprecated public ProvidedItemBuilder durability(final int durability) {
        return this.transform(is -> is.setDurability((short) durability));
    }

    @Deprecated public ProvidedItemBuilder data(final int data) { return this.durability(data); }

    public ProvidedItemBuilder amount(final int amount) { return this.transform(is -> is.setAmount(amount)); }

    public ProvidedItemBuilder enchant(final Enchantment ench, final int level) {
        return this.transform(is -> is.addUnsafeEnchantment(ench, level));
    }

    public ProvidedItemBuilder enchant(final Enchantment ench) { return this.enchant(ench, 1); }

    public ProvidedItemBuilder enchants(final Map<Enchantment, Integer> ench) {
        return this.transformMeta(meta -> ench.forEach((e, l) -> meta.addEnchant(e, l, true)));
    }

    public ProvidedItemBuilder unEnchant(final Enchantment enchantment) {
        return this.transform(is -> is.removeEnchantment(enchantment));
    }

    public ProvidedItemBuilder clearEnchantments() {
        return this.transform(is -> is.getEnchantments().keySet().forEach(is::removeEnchantment));
    }

    public ProvidedItemBuilder color(final Color color) {
        return this.transform(is -> {
            final Material t = is.getType();
            if (t == Material.LEATHER_BOOTS || t == Material.LEATHER_CHESTPLATE ||
                    t == Material.LEATHER_HELMET || t == Material.LEATHER_LEGGINGS) {
                final LeatherArmorMeta m = (LeatherArmorMeta) is.getItemMeta();
                m.setColor(color);
                is.setItemMeta(m);
            }
        });
    }

    public ProvidedItemBuilder owningPlayer(final OfflinePlayer offline) {
        return this.transform(is -> {
            final Material t = is.getType();
            if (t == Material.PLAYER_HEAD || t == Material.PLAYER_WALL_HEAD) {
                final SkullMeta m = (SkullMeta) is.getItemMeta();
                m.setOwningPlayer(offline);
                is.setItemMeta(m);
            }
        });
    }

    public ProvidedItemBuilder owningPlayer(final UUID uuid) { return this.owningPlayer(Bukkit.getOfflinePlayer(uuid)); }

    public ProvidedItemBuilder skinTexture(final String texture, final @Nullable String signature) {
        return this.transform(is -> {
            final Material t = is.getType();
            if (t == Material.PLAYER_HEAD || t == Material.PLAYER_WALL_HEAD) {
                final SkullMeta m = (SkullMeta) is.getItemMeta();
                final PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
                profile.setProperty(new com.destroystokyo.paper.profile.ProfileProperty("textures", texture, signature));
                m.setPlayerProfile(profile);
                is.setItemMeta(m);
            }
        });
    }

    public ProvidedItemBuilder damage(final int damage) {
        return this.transformMeta(meta -> ((Damageable) meta).setDamage(damage));
    }

    public ProvidedItemBuilder flag(final ItemFlag... flags) {
        return this.transformMeta(meta -> meta.addItemFlags(flags));
    }

    public ProvidedItemBuilder unFlag(final ItemFlag... flags) {
        return this.transformMeta(meta -> meta.removeItemFlags(flags));
    }

    public ProvidedItemBuilder flagAll() { return this.flag(ALL_FLAGS); }
    public ProvidedItemBuilder unFlagAll() { return this.unFlag(ALL_FLAGS); }

    public ProvidedItemBuilder customModelData(final Integer cmd) {
        return this.transformMeta(meta -> meta.setCustomModelData(cmd));
    }

    public ProvidedItemBuilder changePersistentData(final Consumer<? super PersistentDataContainer> consumer) {
        return this.transformMeta(meta -> consumer.accept(meta.getPersistentDataContainer()));
    }

    public <T, Z> ProvidedItemBuilder persistentData(final NamespacedKey key, final PersistentDataType<T, Z> type, final Z value) {
        return this.changePersistentData(data -> data.set(key, type, value));
    }

    public ProvidedItemBuilder persistentData(final NamespacedKey key, final byte v) { return this.persistentData(key, PersistentDataType.BYTE, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final byte[] v) { return this.persistentData(key, PersistentDataType.BYTE_ARRAY, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final double v){ return this.persistentData(key, PersistentDataType.DOUBLE, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final float v) { return this.persistentData(key, PersistentDataType.FLOAT, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final int v)   { return this.persistentData(key, PersistentDataType.INTEGER, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final int[] v) { return this.persistentData(key, PersistentDataType.INTEGER_ARRAY, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final long v)  { return this.persistentData(key, PersistentDataType.LONG, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final long[] v){ return this.persistentData(key, PersistentDataType.LONG_ARRAY, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final short v) { return this.persistentData(key, PersistentDataType.SHORT, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final String v){ return this.persistentData(key, PersistentDataType.STRING, v); }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final PersistentDataContainer v) {
        return this.persistentData(key, PersistentDataType.TAG_CONTAINER, v);
    }
    public ProvidedItemBuilder persistentData(final NamespacedKey key, final PersistentDataContainer[] v) {
        return this.persistentData(key, PersistentDataType.TAG_CONTAINER_ARRAY, v);
    }

    @SuppressWarnings({"unchecked"})
    public <IM extends ItemMeta> ProvidedItemBuilder changeMeta(final Consumer<IM> consumer) {
        return this.transformMeta(m -> consumer.accept((IM) m));
    }

    public ProvidedItemBuilder unbreakable() { return breakable(false); }

    public ProvidedItemBuilder breakable(final boolean flag) {
        return this.transformMeta(meta -> meta.setUnbreakable(!flag));
    }

    public ProvidedItemBuilder apply(@NotNull final Consumer<ProvidedItemBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    public ItemStack build() { return this.itemStack; }
}
