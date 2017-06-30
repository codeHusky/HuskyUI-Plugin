/*
 * This file is part of HuskyUI.
 *
 * HuskyUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HuskyUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HuskyUI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codehusky.huskyui.states;

import com.codehusky.huskyui.HuskyUI;
import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.element.Element;
import com.google.common.collect.Maps;
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
import com.codehusky.huskyui.states.element.ActionableElement;
import org.spongepowered.api.text.format.TextStyles;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * An inventory view state. Traditional crate gui view and such.
 * Generally, when making guis custom, overriding the inventory class is a good idea.
 */
public class Page extends State {

    @Nonnull public static ItemStack defaultEmptyStack = ItemStack.builder()
            .itemType(ItemTypes.STAINED_GLASS_PANE)
            .add(Keys.DYE_COLOR, DyeColors.BLACK)
            .add(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_GRAY, "HuskyUI")).build();

    @Nonnull private final String id;
    @Nonnull private final Map<Integer, Element> elements;
    @Nonnull private final InventoryDimension inventoryDimension;
    @Nonnull private final Text title;
    @Nonnull private final ItemStack emptyStack;
    private final boolean fillWhenEmpty;
    private final boolean autoPaging;
    private final boolean centered;
    private final int rows;

    public Page(@Nonnull final String id,
                @Nonnull final Map<Integer, Element> elements,
                @Nonnull final InventoryDimension inventoryDimension,
                @Nonnull final Text title,
                @Nonnull final ItemStack emptyStack,
                final boolean fillWhenEmpty,
                final boolean autoPaging,
                final boolean centered,
                final int rows) {
        this.id = id;
        this.elements = elements;
        this.inventoryDimension = inventoryDimension;
        this.title = title;
        this.emptyStack = emptyStack;
        this.fillWhenEmpty = fillWhenEmpty;
        this.autoPaging = autoPaging;
        this.centered = centered;
        this.rows = rows;
    }

    @Nonnull
    public String getId() {
        return this.id;
    }

    @Nonnull
    public Map<Integer, Element> getElements() {
        return this.elements;
    }

    @Nonnull
    public InventoryDimension getInventoryDimension() {
        return this.inventoryDimension;
    }

    @Nonnull
    public Text getTitle() {
        return this.title;
    }

    @Nonnull
    public ItemStack getEmptyStack() {
        return this.emptyStack;
    }

    public boolean isFillWhenEmpty() {
        return this.fillWhenEmpty;
    }

    public boolean isAutoPaging() {
        return this.autoPaging;
    }

    public int getRows() {
        return this.rows;
    }

    @Nonnull
    public Inventory generatePageView() {
        final Inventory inventory = Inventory.builder()
                .property(InventoryDimension.PROPERTY_NAME, this.inventoryDimension)
                .listener(InteractInventoryEvent.class, event -> {
                    if (!(event instanceof InteractInventoryEvent.Open)
                            && !(event instanceof InteractInventoryEvent.Close)) {
                        event.setCancelled(true);

                        if (event.getCursorTransaction().getDefault().getType() != ItemTypes.AIR) {
                            event.getCursorTransaction().getDefault().toContainer().get(DataQuery.of("UnsafeData", "slotnum")).ifPresent(slot -> {
                                final int num = (int) slot;
                                if (this.autoPaging) {
                                    if (num == -1) {
                                        this.getContainer().openState(this.getObserver(), this.getParent());
                                    } else if (this.elements.get(num) instanceof ActionableElement) {
                                        ((ActionableElement) this.elements.get(num)).getAction().runAction(this.id);
                                    }
                                } else {
                                    if (this.elements.get(num) instanceof ActionableElement) {
                                        ((ActionableElement) this.elements.get(num)).getAction().runAction(this.id);
                                    }
                                }
                            });
                        }
                    }
                })
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(this.title))
                .build(HuskyUI.getInstance());

        int num = 0;
        for (final Inventory slot : inventory.slots()) {
            if (this.autoPaging) {
                if (this.elements.size() > num) {
                    /*if (this.centered && (num >= ((this.rows - 1) * 9))) {
                        if (this.elements.size() %2 == 1) {
                            // TODO: This seems to be unused?
                            // Yes it is. Indev stuff.
                            int width = this.elements.size() % 9;
                        }
                    }*/

                    slot.set(ItemStack.builder()
                            .fromContainer(this.elements.get(num).getItem()
                                    .toContainer()
                                    .set(DataQuery.of("UnsafeData", "slotnum"), num))
                            .build());
                } else if (num > (this.rows * 9) - 1) {
                    if (num == (this.rows * 9) + 4) {
                        slot.set(ItemStack.builder()
                                .fromContainer(ItemStack.builder()
                                        .itemType(ItemTypes.BARRIER)
                                        .add(Keys.DISPLAY_NAME, Text.of(TextStyles.RESET, TextColors.WHITE, "Back"))
                                        .build()
                                        .toContainer()
                                        .set(DataQuery.of("UnsafeData", "slotnum"), -1))
                                .build());
                    }else{
                        slot.set(emptyStack);
                    }
                }
            } else if (elements.containsKey(num)) { // Standard Situations
                slot.set(ItemStack.builder()
                        .fromContainer(elements.get(num).getItem().toContainer()
                                .set(DataQuery.of("UnsafeData", "slotnum"), num))
                        .build());
            }

            num++;
        }

        return inventory;
    }

    @Override
    public void setObserver(final Player observer) {
        if(observer == null) {
            return;
        }

        super.setObserver(observer);

        for(final Element e : elements.values()){
            if(e instanceof ActionableElement){
                ((ActionableElement)e).getAction().setObserver(observer);
            }
        }
    }

    @Nonnull
    @Override
    public Page copy(@Nonnull final StateContainer newContainer) {
        final PageBuilder builder = builder();

        for (final Map.Entry<Integer, Element> entry : this.elements.entrySet()) {
            builder.putElement(entry.getKey(), entry.getValue().copy(newContainer));
        }

        builder.setInventoryDimension(this.inventoryDimension); // InventoryDimension is Immutable.
        builder.setTitle(this.title); // Text is Immutable
        builder.setEmptyStack(this.emptyStack.copy());
        builder.setFillWhenEmpty(this.fillWhenEmpty);
        builder.setAutoPaging(this.autoPaging);
        builder.setCentered(this.centered);

        final Page page = builder.build(this.id);

        page.setContainer(newContainer);
        page.setObserver(this.getObserver());
        page.setParent(this.getParent());

        return page;
    }

    public static PageBuilder builder() {
        return new PageBuilder();
    }

    public static class PageBuilder {

        @Nonnull private final Map<Integer, Element> elements;
        private InventoryDimension inventoryDimension;
        private Text title;
        private ItemStack emptyStack;
        private boolean fillWhenEmpty;
        private boolean autoPaging;
        private boolean centered;

        private PageBuilder() {
            this.elements = Maps.newHashMap();
            this.inventoryDimension = null;
            this.title = Text.of("Unnamed Page");
            this.emptyStack = Page.defaultEmptyStack;
            this.fillWhenEmpty = false;
            this.autoPaging = false;
            this.centered = true;
        }

        public PageBuilder putElement(final int slot, @Nonnull final Element element) {
            this.elements.put(slot, element);
            return this;
        }

        public PageBuilder addElement(@Nonnull final Element element) {
            this.elements.put(this.elements.size(), element);
            return this;
        }

        @Nonnull
        public PageBuilder setInventoryDimension(@Nonnull final InventoryDimension inventoryDimension) {
            this.inventoryDimension = inventoryDimension;
            return this;
        }

        @Nonnull
        public PageBuilder setTitle(@Nonnull final Text title) {
            this.title = title;
            return this;
        }

        @Nonnull
        public PageBuilder setEmptyStack(@Nonnull final ItemStack emptyStack) {
            this.emptyStack = emptyStack;
            return this;
        }

        @Nonnull
        public PageBuilder setFillWhenEmpty(final boolean fillWhenEmpty) {
            this.fillWhenEmpty = fillWhenEmpty;
            return this;
        }

        @Nonnull
        public PageBuilder setAutoPaging(final boolean autoPaging) {
            this.autoPaging = autoPaging;
            return this;
        }

        @Nonnull
        public PageBuilder setCentered(final boolean centered) {
            this.centered = centered;
            return this;
        }

        public Page build(@Nonnull final String id) {
            final int rows = (int) Math.ceil(((double) this.elements.size()) / 9d);
            return new Page(id,
                    this.elements,
                    (this.autoPaging ? InventoryDimension.of(9, rows + 1) : this.inventoryDimension != null ?
                            this.inventoryDimension : InventoryDimension.of(9, rows + 1)),
                    this.title,
                    this.emptyStack,
                    this.fillWhenEmpty,
                    this.autoPaging,
                    this.centered,
                    rows
            );
        }
    }
}
