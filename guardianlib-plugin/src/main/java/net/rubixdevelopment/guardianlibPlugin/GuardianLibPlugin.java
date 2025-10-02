package net.rubixdevelopment.guardianlibPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.rubixdevelopment.guardianlibApi.GuardianLib;
import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.interfaces.*;
import net.rubixdevelopment.guardianlibPlugin.impl.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class GuardianLibPlugin extends JavaPlugin implements GuardianLib {


    private Database db;
    private GuiFactory gui;
    private Scoreboards scoreboards;
    private Text text;
    private Schedulers schedulers;
    private Items items;
    private Players players;

    private HikariDataSource ds;

    @Override public void onLoad() {
        saveDefaultConfig();
        // Hikari init (optioneel – zet je DB config hier of via andere plugin)
        HikariConfig hc = new HikariConfig();
        String url = getConfig().getString("db.url","jdbc:h2:mem:guardianlib"); // placeholder
        hc.setJdbcUrl(url);
        hc.setMaximumPoolSize(10);
        ds = new HikariDataSource(hc);

        db = new DatabaseImpl(ds);
        gui = new GuiFactoryImpl(this);
        scoreboards = new ScoreboardsImpl();
        text = new TextImpl();
        schedulers = new SchedulersImpl(this);
        items = new ItemsImpl();
        players = new PlayersImpl();

        GuardianLibProvider.register(this);
    }

    @Override public void onDisable() {
        if (ds != null) ds.close();
    }

    // GuardianLib getters
    @Override public JavaPlugin getPlugin() { return this; }
    @Override public Database db() { return db; }
    @Override public GuiFactory gui() { return gui; }
    @Override public Scoreboards scoreboards() { return scoreboards; }
    @Override public Text text() { return text; }
    @Override public Schedulers schedulers() { return schedulers; }
    @Override public Items items() { return items; }
    @Override public Players players() { return players; }
}
