package net.rubixdevelopment.guardianlibApi.interfaces;

/*
 Created and owned by FloorNectarine
 Created on 01/10/2025 at time 17:30
*/

public interface Schedulers {
    void runSync(Runnable r);
    void runAsync(Runnable r);
    void runLater(Runnable r, long ticks);
    void runTimer(Runnable r, long delay, long period);
}
