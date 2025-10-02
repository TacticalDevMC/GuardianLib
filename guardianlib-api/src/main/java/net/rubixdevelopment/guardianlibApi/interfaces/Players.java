package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:36
*/

import org.bukkit.entity.Player;

public interface Players {
    void msg(Player p, String legacy);
    void title(Player p, String title, String subtitle, int in, int stay, int out);
    void sound(Player p, org.bukkit.Sound s, float vol, float pitch);
}
