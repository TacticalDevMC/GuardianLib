package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:26
*/

import org.bukkit.entity.Player;

import java.util.List;

public interface Scoreboards {
    Board create(String title, List<String> lines);
    interface Board {
        void show(Player p);
        void hide(Player p);
        void updateTitle(String title);
        void updateLines(List<String> lines);
        void animateTitle(List<String> frames, int ticks);
    }
}
