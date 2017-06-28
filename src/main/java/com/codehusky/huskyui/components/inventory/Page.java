package com.codehusky.huskyui.components.inventory;

import com.codehusky.huskyui.HUIPlugin;
import com.codehusky.huskyui.HuskyUI;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import com.codehusky.huskyui.components.State;
import com.codehusky.huskyui.components.inventory.elements.ActionElement;

import java.util.HashMap;

/**
 * An inventory view state. Traditional crate gui view and such.
 * Generally, when making guis custom, overriding the inventory class is a good idea.
 */
public class Page extends State {
    private HashMap<Integer, Element> elements = new HashMap<>();
    private InventoryDimension inventoryDimension = InventoryDimension.of(9, 1);
    private boolean fillEmptyWithItem = false;
    private Text title = Text.of("Unnamed Page");
    private ItemStack empty = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.DYE_COLOR, DyeColors.BLACK).add(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_GRAY, "HuskyUI")).build();

    public Page(String id) {
        super(id);
    }

    public Page(String id, InventoryDimension invD) {
        super(id);
        this.setInventoryDimension(invD);
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public void setInventoryDimension(InventoryDimension invD) {
        inventoryDimension = invD;
    }

    public void putElement(int slotnum, Element element) {
        elements.put(slotnum, element);
    }

    public Inventory generatePageView() {
        Inventory ourInventory = Inventory.builder()
                .property(InventoryDimension.PROPERTY_NAME, inventoryDimension)
                .listener(InteractInventoryEvent.class, evt -> {
                    if (!(evt instanceof InteractInventoryEvent.Open) && !(evt instanceof InteractInventoryEvent.Close)) {
                        evt.setCancelled(true);
                        //clickable
                        if (evt.getCursorTransaction().getDefault().getType() != ItemTypes.AIR) {
                            if (evt.getCursorTransaction().getDefault().toContainer().get(DataQuery.of("UnsafeData", "slotnum")).isPresent()) {
                                int slotnum = (int) evt.getCursorTransaction().getDefault().toContainer().get(DataQuery.of("UnsafeData", "slotnum")).get();
                                if (elements.get(slotnum) instanceof ActionElement) {
                                    ((ActionElement) elements.get(slotnum)).runAction(getId());
                                }
                            }
                        }
                    }
                })
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(title))
                .build(HUIPlugin.getInstance());

        int slotNum = 0;
        for (Inventory slot : ourInventory.slots()) {
            if (elements.containsKey(slotNum)) {
                ItemStack orig = elements.get(slotNum).getDisplayItem();
                ItemStack modifiedStack = ItemStack.builder().fromContainer(orig.toContainer().set(DataQuery.of("UnsafeData", "slotnum"), slotNum)).build();
                slot.set(modifiedStack);
            } else if (fillEmptyWithItem) {
                slot.set(empty);
            }
            slotNum++;
        }
        return ourInventory;
    }

    public HashMap<Integer, Element> getElements() {
        return elements;
    }

    public InventoryDimension getInventoryDimension() {
        return inventoryDimension;
    }

    public ItemStack getEmpty() {
        return empty;
    }

    public Text getTitle() {
        return title;
    }
}
