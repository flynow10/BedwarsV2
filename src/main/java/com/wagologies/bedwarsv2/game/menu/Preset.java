package com.wagologies.bedwarsv2.game.menu;

import com.wagologies.bedwarsv2.game.Game;

import java.util.List;

public interface Preset {

    void setGame(Game game);

    String getName();

    int getSize();

    List<Item> getItems();
}
