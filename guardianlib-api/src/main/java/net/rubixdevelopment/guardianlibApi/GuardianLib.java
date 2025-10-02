package net.rubixdevelopment.guardianlibApi;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:11
*/

import net.rubixdevelopment.guardianlibApi.interfaces.*;
import org.bukkit.plugin.Plugin;

public interface GuardianLib {

    static GuardianLib get() {
        return GuardianLibProvider.get();
    }

    Plugin getPlugin();

    Database db();
    GuiFactory gui();
    Scoreboards scoreboards();
    Text text();
    Schedulers schedulers();
    Items items();
    Players players();

}
