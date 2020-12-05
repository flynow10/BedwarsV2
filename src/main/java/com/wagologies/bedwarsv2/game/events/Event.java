package com.wagologies.bedwarsv2.game.events;

import com.wagologies.bedwarsv2.game.Game;

import java.util.function.Consumer;

public class Event {
    private String name;
    private int secondsUntilEvent;
    private Consumer<Game> callback;
    public Event(String name)
    {
        this.name = name;
    }

    public Event(String name, int secondsUntilEvent)
    {
        this(name);
        this.secondsUntilEvent = secondsUntilEvent;
    }

    public Event(String name, int secondsUntilEvent, Consumer<Game> callback)
    {
        this(name, secondsUntilEvent);
        this.callback = callback;
    }
    public void RunEvent(Game game)
    {
        callback.accept(game);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSecondsUntilEvent(int secondsUntilEvent)
    {
        this.secondsUntilEvent = secondsUntilEvent;
    }

    public void setCallback(Consumer<Game> callback)
    {
        this.callback = callback;
    }

    public String getName() { return name; }

    public int getSecondsUntilEvent() { return secondsUntilEvent; }
}
