package net.rubixdevelopment.guardianlibPlugin.impl;

import net.rubixdevelopment.guardianlibApi.interfaces.Schedulers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SchedulersImpl implements Schedulers {
    private final Plugin p;
    public SchedulersImpl(Plugin p){ this.p=p; }
    @Override public void runSync(Runnable r){ Bukkit.getScheduler().runTask(p, r); }
    @Override public void runAsync(Runnable r){ Bukkit.getScheduler().runTaskAsynchronously(p, r); }
    @Override public void runLater(Runnable r, long ticks){ Bukkit.getScheduler().runTaskLater(p, r, ticks); }
    @Override public void runTimer(Runnable r, long delay, long period){ Bukkit.getScheduler().runTaskTimer(p, r, delay, period); }
}