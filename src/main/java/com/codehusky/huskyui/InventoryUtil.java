package com.codehusky.huskyui;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.scheduler.Task;

public class InventoryUtil {
    public static void close(Player player) {
        Task.builder().execute(()->
            player.closeInventory()
        ).delayTicks(1).submit(HuskyUI.getInstance());
    }

    public static void open(Player player, Inventory inventory) {
        Task.builder().execute(()-> {
            player.closeInventory();
            player.openInventory(inventory);
        }).delayTicks(1).submit(HuskyUI.getInstance());
    }

}
