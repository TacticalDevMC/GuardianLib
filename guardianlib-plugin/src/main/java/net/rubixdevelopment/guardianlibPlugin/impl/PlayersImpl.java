package net.rubixdevelopment.guardianlibPlugin.impl;

import net.kyori.adventure.title.Title;
import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.interfaces.Players;
import net.rubixdevelopment.guardianlibApi.interfaces.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Duration;

public class PlayersImpl implements Players {
    private final Text text = GuardianLibProvider.get().text();
    @Override public void msg(Player p, String legacy){ p.sendMessage(text.component(legacy)); }
    @Override public void title(Player p, String t, String s, int in, int stay, int out){
        p.showTitle(Title.title(text.component(t), text.component(s),
                Title.Times.times(Duration.ofMillis(in*50L), Duration.ofMillis(stay*50L), Duration.ofMillis(out*50L))));
    }
    @Override public void sound(Player p, Sound so, float v, float pi){ p.playSound(p.getLocation(), so, v, pi); }
}