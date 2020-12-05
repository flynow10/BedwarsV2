package com.wagologies.bedwarsv2.game;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    public HashMap<UUID, FastBoard> playerBoards = new HashMap<>();

    public Scoreboard scoreboard;

    public String boardName;

    public ScoreboardManager(Scoreboard scoreboard, String boardName)
    {
        this.scoreboard = scoreboard;
        this.boardName = boardName;
    }

    public void AddPlayer(Player...players)
    {
        for (Player player : players) {
            FastBoard board = new FastBoard(player);
            board.updateTitle(boardName);
            playerBoards.put(player.getUniqueId(), board);
        }
    }

    public void SetLines(Player player, String...lines)
    {
        if(playerBoards.containsKey(player.getUniqueId()))
        {
            playerBoards.get(player.getUniqueId()).updateLines(lines);
        }
    }

    public void SetLines(String...lines)
    {
        for (FastBoard value : playerBoards.values()) {
            value.updateLines(lines);
        }
    }

    public void Set(Player player, String text, Integer line)
    {
        if(playerBoards.containsKey(player.getUniqueId()))
        {
            playerBoards.get(player.getUniqueId()).updateLine(line, text);
        }
    }

    public void Set(String text, Integer line)
    {
        for (FastBoard value : playerBoards.values()) {
            value.updateLine(line, text);
        }
    }

    public void SetName(Player player, String name)
    {
        boardName = name;
        if(playerBoards.containsKey(player.getUniqueId()))
        {
            playerBoards.get(player.getUniqueId()).updateTitle(name);
        }
    }

    public void SetName(String name)
    {
        boardName = name;
        for (FastBoard value : playerBoards.values()) {
            value.updateTitle(name);
        }
    }
}
