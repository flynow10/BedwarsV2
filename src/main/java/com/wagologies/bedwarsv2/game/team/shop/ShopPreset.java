package com.wagologies.bedwarsv2.game.team.shop;

import com.wagologies.bedwarsv2.game.team.shop.item.ShopCategory;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopItem;

import java.util.ArrayList;
import java.util.List;

public interface ShopPreset {
    ShopCategory getFirstCategory();
    List<ShopItem> getShopItems();
    List<ShopCategory> getCategories();
    String getName();
}
