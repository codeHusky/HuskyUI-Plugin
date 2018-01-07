package com.codehusky.huskyui;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.property.StringProperty;

import java.util.Optional;

public class InventoryUtil {
    public static void close(Player player) {
        player.closeInventory(HuskyUI.getInstance().getGenericCause());
    }

    public static void open(Player player, Inventory inventory) {
        player.openInventory(inventory,HuskyUI.getInstance().getGenericCause());
    }

}
