package com.wagologies.bedwarsv2.game.events;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitScheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class EventManager {

    private Queue<Event> eventQueue = new LinkedList<>();
    private boolean shouldRunEventNow = false;
    private Game game;

    public EventManager(Game game)
    {
        this.game = game;
        game.getScoreboardManager().Set(ChatColor.RED + "No Upcoming Event", 2);
    }

    private void NextEvent()
    {
        if(game.getIsRunning()) {
            if (!eventQueue.isEmpty()) {
                Timer(eventQueue.peek().getSecondsUntilEvent());
            }
            else
            {
                game.getScoreboardManager().Set(ChatColor.RED + "No Upcoming Event", 2);
            }
        }
    }

    private void Timer(int seconds)
    {
        if(game.getIsRunning()) {
            if (seconds == 0 || shouldRunEventNow) {
                shouldRunEventNow = false;
                try {
                    RunEvent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NextEvent();
            }
            else {
                UpdateScoreBoard(seconds);
                Bukkit.getScheduler().runTaskLater(BedwarsV2.getInstance(), () -> Timer(seconds - 1), 20);
            }
        }
    }

    private void UpdateScoreBoard(int seconds)
    {
        Date date = new Date(seconds*1000);
        String time = new SimpleDateFormat("mm:ss").format(date);
        if (eventQueue.peek() != null) {
            game.getScoreboardManager().Set(eventQueue.peek().getName() + ChatColor.WHITE + " in:", 2);
            game.getScoreboardManager().Set("( " + ChatColor.GOLD + time + ChatColor.WHITE + " )", 3);
        }
    }

    private void RunEvent()
    {
        if(game.getIsRunning()) {
            Event event = eventQueue.poll();
            if (event != null)
                event.RunEvent(game);
        }
    }

    public void RunEventNow()
    {
        shouldRunEventNow = true;
    }

    public void AddQueueItems(Event...events)
    {
        boolean wasEmpty = eventQueue.isEmpty();
        for (Event event : events) {
            eventQueue.add(event);
        }
        if(wasEmpty)
            NextEvent();
    }
}
