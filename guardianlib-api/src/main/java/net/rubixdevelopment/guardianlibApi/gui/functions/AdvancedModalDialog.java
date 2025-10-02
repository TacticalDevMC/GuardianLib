package net.rubixdevelopment.guardianlibApi.gui.functions;
/*
 Created and owned by TacticalDev
 Created on 01/12/2024 at time 12:05
*/

import net.rubixdevelopment.guardiancore.utils.ColorFormat;
import net.rubixdevelopment.guardiancore.utils.gui.GUI;
import net.rubixdevelopment.guardiancore.utils.gui.button.Button;
import net.rubixdevelopment.guardiancore.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.function.Consumer;


public class AdvancedModalDialog extends GUI {

    public AdvancedModalDialog(Plugin plugin, String question, Map<String, Consumer<Player>> options) {
        super(plugin, question, 1);

        int index = 0;
        for (Map.Entry<String, Consumer<Player>> option : options.entrySet()) {
            addButton(index++, new Button(
                    new ItemBuilder(Material.PAPER).name(ColorFormat.color(option.getKey())).build(),
                    player -> {
                        option.getValue().accept(player);
                        player.closeInventory();
                    }));
        }
    }
}
