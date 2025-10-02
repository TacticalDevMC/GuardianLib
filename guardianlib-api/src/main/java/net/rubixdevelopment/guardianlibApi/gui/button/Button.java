package net.rubixdevelopment.guardianlibApi.gui.button;

import net.rubixdevelopment.guardianlibApi.gui.button.functions.ClickContext;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Button {

    private ItemStack icon;
    private String id, label;
    private List<Consumer<ClickContext>> clickHandlers;
    private BiConsumer<Player, ClickType> clickTypeHandler;
    private Consumer<Player> onClick;
    private boolean useSounds;
    private Sound sound;


    public Button(ItemStack icon, Consumer<Player> onClick) {
        this.icon = icon;
        this.onClick = onClick;
    }

    public Button(ItemStack icon, String id, String label) {
        this.icon = icon;
        this.id = id;
        this.label = label;
        this.clickHandlers = new ArrayList<>();
    }

    public Button(ItemStack icon, boolean useSound, Sound sound, Consumer<Player> onClick) {
        this.icon = icon;
        this.onClick = onClick;
        this.useSounds = useSound;
        this.sound = sound;
    }

    public Button(ItemStack icon, Sound sound, Consumer<ClickContext> clickHandler) {
        this.icon = icon;
        this.useSounds = true;
        this.sound = sound;
        this.clickHandlers = new ArrayList<>();
        this.clickHandlers.add(clickHandler);
    }

    public Button(ItemStack icon, boolean useSound, Sound sound, String id, String label) {
        this.icon = icon;
        this.id = id;
        this.label = label;
        this.clickHandlers = new ArrayList<>();
        this.useSounds = useSound;
        this.sound = sound;
    }

    public void addClickHandler(Consumer<ClickContext> onClick) {
        if (clickHandlers == null) {
            this.clickHandlers = new ArrayList<>();
        }
        this.clickHandlers.add(onClick);
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public Button setName(String name) {
        if (name != null) {
            Objects.requireNonNull(getIcon().getItemMeta()).setDisplayName(name);
        }
        return this;
    }

    public void onClickHandler(ClickContext context) {
        for (Consumer<ClickContext> handler : clickHandlers) {
            handler.accept(context);
            if (isUseSounds() && getSound() != null) {
                Location location = context.getPlayer().getLocation();

                context.getPlayer().getWorld().playSound(location, sound, 1, 1);
            }
        }

        if (clickTypeHandler != null && context.getClick() != null) {
            clickTypeHandler.accept(context.getPlayer(), context.getClick());
        }
    }

    public void onClick(Player player) {
        onClick.accept(player);
        if (isUseSounds() && getSound() != null) {
            Location location = player.getLocation();

            player.getWorld().playSound(location, sound, 1, 1);
        }
    }

    public Consumer<Player> getOnClick() {
        return onClick;
    }

    public List<Consumer<ClickContext>> getClickHandlers() {
        return clickHandlers;
    }

    public void setClickTypeHandler(BiConsumer<Player, ClickType> clickTypeHandler) {
        this.clickTypeHandler = clickTypeHandler;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setUseSounds(boolean useSounds) {
        this.useSounds = useSounds;
    }

    public boolean isUseSounds() {
        return useSounds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}