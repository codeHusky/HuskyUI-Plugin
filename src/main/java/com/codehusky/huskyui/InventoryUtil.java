package com.codehusky.huskyui;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.property.StringProperty;

import java.util.Optional;

public class InventoryUtil {
    public static void close(Player player) {
        check(player.getOpenInventory()).ifPresent(Inventory::clear);
        player.closeInventory();
    }

    public static void open(Player player, Inventory inventory) {
        check(player.getOpenInventory()).ifPresent(Inventory::clear);
        player.openInventory(inventory);
    }

    public static Optional<Container> check(Optional<Container> inventory) {
        return inventory.filter(i -> i.getProperties(StringProperty.class).stream().anyMatch(p -> p.equals(StringProperty.of("huskui-page"))));
    }
}
