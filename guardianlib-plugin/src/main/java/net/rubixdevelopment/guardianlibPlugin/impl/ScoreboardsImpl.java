package net.rubixdevelopment.guardianlibPlugin.impl;

import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.interfaces.Scoreboards;
import net.rubixdevelopment.guardianlibApi.interfaces.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardsImpl implements Scoreboards {
    private final Map<UUID, BoardImpl> active = new ConcurrentHashMap<>();
    private final Text text = GuardianLibProvider.get().text();

    @Override public Board create(String title, List<String> lines) { return new BoardImpl(title, lines); }

    private final class BoardImpl implements Board {
        private String title;
        private List<String> lines;
        private final Scoreboard sb;
        private final Objective obj;

        BoardImpl(String title, List<String> lines){
            this.title = title; this.lines = new ArrayList<>(lines);
            ScoreboardManager mgr = Bukkit.getScoreboardManager();
            sb = Objects.requireNonNull(mgr).getNewScoreboard();
            obj = sb.registerNewObjective("gblib-"+UUID.randomUUID(), Criteria.DUMMY, text.component(title));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            render();
        }

        private void render(){
            // clear
            for (String e : sb.getEntries()) sb.resetScores(e);
            // set lines top->bottom
            int score = lines.size();
            for (String l : lines) {
                String entry = UUID.randomUUID().toString().substring(0, 16);
                Team t = sb.registerNewTeam("t"+UUID.randomUUID().toString().substring(0,10));
                t.addEntry(entry);
                t.prefix(text.component(l));
                obj.getScore(entry).setScore(score--);
            }
        }

        @Override public void show(Player p){ p.setScoreboard(sb); active.put(p.getUniqueId(), this); }
        @Override public void hide(Player p){ if (active.remove(p.getUniqueId()) != null) p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()); }
        @Override public void updateTitle(String t){ this.title=t; obj.displayName(text.component(t)); }
        @Override public void updateLines(List<String> ls){ this.lines=new ArrayList<>(ls); render(); }

        @Override public void animateTitle(List<String> frames, int ticks) {
            final int[] i = {0};
            Bukkit.getScheduler().runTaskTimer(GuardianLibProvider.get().getPlugin(), () -> {
                String f = frames.get(i[0] % frames.size());
                updateTitle(f);
                i[0]++;
            }, 0L, ticks);
        }
    }
}