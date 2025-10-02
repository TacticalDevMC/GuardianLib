package net.rubixdevelopment.guardianlibApi.gui.functions;
/*
 Created and owned by TacticalDev
 Created on 01/12/2024 at time 0:49
*/

import net.rubixdevelopment.guardianlibApi.gui.GUI;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ProgressBar {

    public static void setProgressBar(GUI gui, int row, double progress, Material filled, Material empty) {
        int slots = gui.getInventory().getSize();
        int startSlot = (row - 1) * 9;

        int filledSlots = (int) (progress * 9);

        for (int i = 0; i < 9; i++) {
            gui.addButton(startSlot + i, new Button(
                    new ItemStack(i < filledSlots ? filled : empty),
                    player -> {
                    }
            ));
        }
    }

}
