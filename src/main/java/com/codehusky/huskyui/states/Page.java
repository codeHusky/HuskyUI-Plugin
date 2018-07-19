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

import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.StringProperty;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.codehusky.huskyui.HuskyUI;
import com.codehusky.huskyui.InventoryUtil;
import com.codehusky.huskyui.StateContainer;
import com.codehusky.huskyui.states.element.ActionableElement;
import com.codehusky.huskyui.states.element.Element;
import com.google.common.collect.Maps;

/**
 * An extension of {@link State}, intended to be used for
 * chest-based GUIs.
 */
public class Page extends State
{

	/**
	 * The default {@link ItemStack} to be used if no ItemStack is defined
	 * for a slot while {@link Page#fillWhenEmpty} is set to <code>true</code>.
	 */
	@Nonnull
	public static ItemStack defaultEmptyStack = ItemStack.builder()
		.itemType(ItemTypes.STAINED_GLASS_PANE)
		.add(Keys.DYE_COLOR, DyeColors.BLACK)
		.add(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_GRAY, "HuskyUI")).build();

	@Nonnull
	public static ItemStack defaultBackStack = ItemStack.builder()
		.itemType(ItemTypes.MAP)
		.add(Keys.DISPLAY_NAME, Text.of(TextStyles.RESET, TextColors.WHITE, "Previous"))
		.build();

	@Nonnull
	public static ItemStack defaultNextStack = ItemStack.builder()
		.itemType(ItemTypes.MAP)
		.add(Keys.DISPLAY_NAME, Text.of(TextStyles.RESET, TextColors.WHITE, "Next"))
		.build();

	@Nonnull
	public static ItemStack defaultCloseStack = ItemStack.builder()
		.itemType(ItemTypes.BARRIER)
		.add(Keys.DISPLAY_NAME, Text.of(TextStyles.RESET, TextColors.WHITE, "Close"))
		.build();

	/**
	 * The {@link Element}s that will be used by this Page.
	 *
	 * <p>Elements are sorted by integer, ostensibly referring
	 * to their placement in the chest.</p>
	 */
	@Nonnull
	private final Map<Integer, Element> elements;

	/**
	 * The {@link InventoryDimension} is the space in which {@link Element}s
	 * will be placed when the inventory is opened by a {@link Player}.
	 */
	@Nonnull
	private final InventoryDimension inventoryDimension;

	@Nonnull
	private final InventoryArchetype inventoryArchetype;

	/**
	 * The name of the Chest to be opened, because IDs
	 * typically aren't very visually appealing.
	 */
	@Nonnull
	private final Text title;

	/**
	 * The {@link ItemStack} to be used when {@link Page#fillWhenEmpty}
	 * is set to <code>true</code>. Usually this will be determined by
	 * {@link Page#defaultEmptyStack}.
	 */
	@Nonnull
	private final ItemStack emptyStack;

	@Nonnull
	private final ItemStack backStack;

	@Nonnull
	private final ItemStack nextStack;

	@Nonnull
	private final ItemStack closeStack;

	/**
	 * Whether or not to fill empty stacks with
	 * predetermined {@link ItemStack}s.
	 */
	private final boolean fillWhenEmpty;

	/**
	 * Whether or not paging should be handled by HuskyUI
	 * or the implementing plugin.
	 */
	private final boolean autoPaging;

	/**
	 * Whether or not to center the {@link ItemStack}.
	 *
	 * <p>Currently serves no purpose.</p>
	 */
	private final boolean centered;

	/**
	 * The number of rows to appear within the {@link InventoryDimension}.
	 *
	 * <p>If {@link Page#autoPaging} is set to <code>true</code>, this value
	 * will be automatically determined based off of the number of items
	 * being digested by the Page.</p>
	 */
	private final int rows;
	private boolean updatable;
	private int updateTickRate;
	private Consumer<Page> updateConsumer;
	private Runnable interrupt;
	private Inventory cachedInventory;

	private Task updaterTask;

	/**
	 * Constructs a Page.
	 *
	 * @param id the ID of the State this Page extends
	 * @param elements the {@link ItemStack}s in use by this Page
	 * @param inventoryDimension the virtual inventory this Page represents
	 * @param title the name of the chest
	 * @param emptyStack the ItemStack to be used if filling blank spaces
	 * @param updatable if the Page is updatable or not
	 * @param updateTickRate if the page is updatable, how many ticks will be between updates.
	 * @param updateConsumer the consumer to run if the page is updatable every {@link Page#updateTickRate} ticks.
	 * @param fillWhenEmpty whether or not to fill blank spaces
	 * @param autoPaging whether or not to let HuskyUI handle paging
	 * @param centered whether or not to center ItemStacks
	 * @param rows the number of rows within the InventoryDimension
	 * @param parent the parent page of this page.
	 */
	public Page(@Nonnull final String id, @Nonnull final Map<Integer, Element> elements, @Nonnull final InventoryDimension inventoryDimension, @Nonnull final InventoryArchetype inventoryArchetype, @Nonnull final Text title, @Nonnull final ItemStack emptyStack, @Nonnull final ItemStack backStack, @Nonnull final ItemStack nextStack, @Nonnull final ItemStack closeStack, final boolean updatable, final int updateTickRate, final Consumer<Page> updateConsumer, final Runnable interrupt, final boolean fillWhenEmpty, final boolean autoPaging, final boolean centered, final int rows, final String parent)
	{
		super(id);
		this.elements = elements;
		this.inventoryDimension = inventoryDimension;
		this.inventoryArchetype = inventoryArchetype;
		this.title = title;

		this.emptyStack = emptyStack;
		this.backStack = backStack;
		this.nextStack = nextStack;
		this.closeStack = closeStack;

		this.fillWhenEmpty = fillWhenEmpty;
		this.autoPaging = autoPaging;
		this.centered = centered;
		this.rows = rows;
		this.updatable = updatable;
		this.updateTickRate = updateTickRate;
		this.updateConsumer = updateConsumer;
		this.interrupt = interrupt;
		cachedInventory = null;

		this.setParent(parent);
	}

	public Consumer<Page> getUpdateConsumer()
	{
		return updateConsumer;
	}

	public int getUpdateTickRate()
	{
		return updateTickRate;
	}

	public boolean isUpdatable()
	{
		return updatable;
	}

	/**
	 * Gets the {@link ItemStack}s in use by this Page.
	 *
	 * @return the ItemStacks in use by this Page
	 */
	@Nonnull
	public Map<Integer, Element> getElements()
	{
		return this.elements;
	}

	/**
	 * Gets the virtual inventory this Page represents.
	 *
	 * @return the virtual inventory this Page represents
	 */
	@Nonnull
	public InventoryDimension getInventoryDimension()
	{
		return this.inventoryDimension;
	}

	/**
	 * Gets the title of the chest this Page uses.
	 *
	 * @return the title of the chest
	 */
	@Nonnull
	public Text getTitle()
	{
		return this.title;
	}

	/**
	 * Gets the {@link ItemStack} to use when an {@link Element}
	 * hasn't been given.
	 *
	 * @return the default ItemStack
	 */
	@Nonnull
	public ItemStack getEmptyStack()
	{
		return this.emptyStack;
	}

	/**
	 * Determines whether or not HuskyUI is replacing non-specified
	 * {@link Element}s with a default {@link ItemStack}.
	 *
	 * @return true if filling empty Elements; false otherwise
	 */
	public boolean isFillWhenEmpty()
	{
		return this.fillWhenEmpty;
	}

	/**
	 * Determines whether or not HuskyUI is handling paging.
	 *
	 * @return true if HuskyUI is handling paging; false otherwise
	 */
	public boolean isAutoPaging()
	{
		return this.autoPaging;
	}

	/**
	 * Gets the number of rows in the {@link InventoryDimension}.
	 *
	 * @return the number of rows in the InventoryDimension
	 */
	public int getRows()
	{
		return this.rows;
	}

	private long ticks = 0;

	public long getActualTicks()
	{
		return ticks;
	}

	public long getTicks()
	{
		return (long) Math.floor(ticks / updateTickRate);
	}

	public void tickIncrement()
	{
		ticks++;
	}

	@Nonnull
	public Inventory getPageView()
	{
		return getPageView(0);
	}

	/**
	 * Generates the {@link Inventory} for this Page.
	 *
	 * @return the Inventory for this Page
	 */
	@Nonnull
	public Inventory getPageView(int pagenum)
	{
		if (updatable && cachedInventory != null)
		{
			return cachedInventory;
		}
		int maxSize = Math.max(1, (this.inventoryDimension.getColumns() * (this.inventoryDimension.getRows() - 1)));
		int pageCount = (this.elements.size() / maxSize) + 1;
		final Inventory inventory = Inventory.builder()
			.property("type", new StringProperty("huskui-page"))
			.property("id", new StringProperty(getId()))
			.property(InventoryDimension.PROPERTY_NAME, this.inventoryDimension)
			.of(this.inventoryArchetype)
			.listener(InteractInventoryEvent.class, event -> {
				if (event instanceof InteractInventoryEvent.Close)
				{
					interrupt();
					return;
				}
				if (!(event instanceof InteractInventoryEvent.Open) && !(event instanceof InteractInventoryEvent.Close))
				{
					event.setCancelled(true);
					try
					{
						if (event.getCursorTransaction().getDefault().getType() == ItemTypes.AIR)
							return;
					}
					catch (NoSuchFieldError err)
					{
						// old api support
						if (event.getCursorTransaction().getDefault().getType() == ItemTypes.NONE)
							return;
					}

					event.getCursorTransaction().getDefault().toContainer().get(DataQuery.of("UnsafeData", "slotnum")).ifPresent(slot -> {
						final int num = (int) slot;

						if (this.autoPaging)
						{
							if (num == -1)
							{
								// close/back
								if (this.hasParent())
								{
									this.getContainer().openState(this.getObserver(), this.getParent());
								}
								else
								{
									InventoryUtil.close(this.getObserver());
								}
							}
							else if (num == -2)
							{ // previous page
								this.getContainer().openState(this.getObserver(), this.getId() + "#" + (pagenum - 1));
							}
							else if (num == -3)
							{ // next page
								this.getContainer().openState(this.getObserver(), this.getId() + "#" + (pagenum + 1));
							}
							else if (this.elements.get(num) instanceof ActionableElement)
							{
								((ActionableElement) this.elements.get(num)).getAction().runAction(this.getId(), getObserver().getOpenInventory().get());
							}
						}
						else
						{
							if (this.elements.get(num) instanceof ActionableElement)
							{
								((ActionableElement) this.elements.get(num)).getAction().runAction(this.getId(), getObserver().getOpenInventory().get());
							}
						}
					});
				}
				else
				{
					event.getCursorTransaction().setCustom(ItemStackSnapshot.NONE);
					event.getCursorTransaction().setValid(true);
				}
			})
			.property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(this.title))
			.build(HuskyUI.getInstance());

		int num = 0;

		for (final Inventory slot : inventory.slots())
		{
			if (this.autoPaging)
			{
				if (num == (this.rows * 9) + 3 && pageCount > 1 && pagenum > 0)
				{
					slot.set(ItemStack.builder()
						.fromContainer(this.backStack
							.toContainer()
							.set(DataQuery.of("UnsafeData", "slotnum"), -2))
						.build());
				}
				else if (num == (this.rows * 9) + 5 && pageCount > 1 && pagenum < pageCount - 1)
				{
					slot.set(ItemStack.builder()
						.fromContainer(this.nextStack
							.toContainer()
							.set(DataQuery.of("UnsafeData", "slotnum"), -3))
						.build());
				}
				else if (num == (this.rows * 9) + 4)
				{
					slot.set(ItemStack.builder()
						.fromContainer(this.closeStack
							.toContainer()
							.set(DataQuery.of("UnsafeData", "slotnum"), -1))
						.build());
				}
				else if (num > (this.rows * 9) - 1)
				{
					slot.set(emptyStack);
				}
				else if (this.elements.containsKey(num + (pagenum * maxSize)))
				{
					slot.set(ItemStack.builder()
						.fromContainer(this.elements.get(num + (pagenum * maxSize)).getItem()
							.toContainer()
							.set(DataQuery.of("UnsafeData", "slotnum"), num + (pagenum * maxSize)))
						.build());
				}

			}
			else if (elements.containsKey(num))
			{ // Standard Situations
				slot.set(ItemStack.builder()
					.fromContainer(elements.get(num).getItem().toContainer()
						.set(DataQuery.of("UnsafeData", "slotnum"), num))
					.build());
			}

			num++;
		}
		if (updatable)
		{
			this.cachedInventory = inventory;
			this.updaterTask = Task.builder().intervalTicks(1).execute(() -> {
				if (this.getActualTicks() % this.getUpdateTickRate() == 0)
				{
					this.updateConsumer.accept(this);
				}
				this.tickIncrement();
			}).submit(HuskyUI.getInstance());
		}
		return inventory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>Additionally, assigns the {@link Player} to all
	 * {@link Element}s within this Page.</p>
	 *
	 * @param observer the Player viewing this State
	 */
	@Override
	public void setObserver(final Player observer)
	{
		if (observer == null)
		{
			return;
		}

		super.setObserver(observer);

		for (final Element e : elements.values())
		{
			if (e instanceof ActionableElement)
			{
				((ActionableElement) e).getAction().setObserver(observer);
			}
		}
	}

	public void interrupt()
	{
		// System.out.println("-++-\nINTERRUPT\nInterrupt Is Null: " + (this.interrupt == null) + "\nUpdater Task Is Null: " + (this.updaterTask == null));

		if (this.interrupt != null)
		{
			try
			{
				this.interrupt.run();
			}
			catch (Exception e)
			{
				HuskyUI.getLogger().error("Error occurred while running HuskyUI Page interrupt.");
				e.printStackTrace();
			}
			// System.out.println("Interrupt ran.");
		}
		if (updaterTask != null)
		{
			updaterTask.cancel();
			// System.out.println("Updater cancelled");
			updaterTask = null;
			// System.out.println("Updater set to null.");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param newContainer the {@link StateContainer} that will be taking
	 *                     responsibility for this Page.
	 * @return a copy of this Page
	 */
	@Nonnull
	@Override
	public Page copy(@Nonnull final StateContainer newContainer)
	{
		final PageBuilder builder = builder();

		for (final Map.Entry<Integer, Element> entry : this.elements.entrySet())
		{
			builder.putElement(entry.getKey(), entry.getValue().copy(newContainer));
		}

		builder.setInventoryDimension(this.inventoryDimension); // InventoryDimension is Immutable.
		builder.setInventoryArchetype(this.inventoryArchetype);
		builder.setTitle(this.title); // Text is Immutable

		builder.setEmptyStack(this.emptyStack.copy());
		builder.setBackStack(this.backStack.copy());
		builder.setNextStack(this.nextStack.copy());
		builder.setCloseStack(this.closeStack.copy());

		builder.setFillWhenEmpty(this.fillWhenEmpty);
		builder.setAutoPaging(this.autoPaging);
		builder.setCentered(this.centered);
		builder.setUpdateTickRate(updateTickRate);
		builder.setUpdater(updateConsumer);
		builder.setInterrupt(interrupt);
		builder.setUpdatable(updatable);

		final Page page = builder.build(this.getId());

		page.setContainer(newContainer);
		page.setObserver(this.getObserver());
		page.setParent(this.getParent());

		return page;
	}

	/**
	 * Creates a new {@link PageBuilder}.
	 *
	 * @return a new PageBuilder
	 */
	public static PageBuilder builder()
	{
		return new PageBuilder();
	}

	/**
	 * An easy-to-use class for quickly creating {@link Page}s.
	 */
	public static class PageBuilder
	{

		/**
		 * The {@link Element}s that will be used by this Page.
		 *
		 * <p>Elements are sorted by integer, ostensibly referring
		 * to their placement in the chest.</p>
		 */
		@Nonnull
		private final Map<Integer, Element> elements;

		/**
		 * The {@link InventoryDimension} is the space in which {@link Element}s
		 * will be placed when the inventory is opened by a {@link Player}.
		 */
		private InventoryDimension inventoryDimension;

		private InventoryArchetype inventoryArchetype;

		/**
		 * The name of the Chest to be opened, because IDs
		 * typically aren't very visually appealing.
		 */
		private Text title;

		/**
		 * The {@link ItemStack} to be used when {@link Page#fillWhenEmpty}
		 * is set to <code>true</code>. Usually this will be determined by
		 * {@link Page#defaultEmptyStack}.
		 */
		private ItemStack emptyStack;

		private ItemStack backStack;
		private ItemStack nextStack;
		private ItemStack closeStack;

		/**
		 * Whether or not to fill empty stacks with
		 * predetermined {@link ItemStack}s.
		 */
		private boolean fillWhenEmpty;

		/**
		 * Whether or not paging should be handled by HuskyUI
		 * or the implementing plugin.
		 */
		private boolean autoPaging;

		/**
		 * Whether or not to center the {@link ItemStack}.
		 *
		 * <p>Currently serves no purpose.</p>
		 */
		private boolean centered;

		/**
		 * Defines if the current page should be checked for updates.
		 */
		private boolean updatable;

		/**
		 * How often the updater should be fired.
		 * Defaults to a 1 tick interval.
		 */
		private int updateTickRate;

		private Consumer<Page> updaterConsumer;

		private Runnable interrupt;

		@Nullable
		private String parent;

		/**
		 * Constructs a new {@link PageBuilder}, currently only
		 * accessible via {@link Page#builder()}.
		 */
		private PageBuilder()
		{
			this.elements = Maps.newHashMap();
			this.inventoryDimension = null;
			this.title = Text.of("Unnamed Page");
			this.emptyStack = Page.defaultEmptyStack;
			this.backStack = Page.defaultBackStack;
			this.nextStack = Page.defaultNextStack;
			this.closeStack = Page.defaultCloseStack;
			this.fillWhenEmpty = false;
			this.autoPaging = false;
			this.centered = true;
			this.updatable = false;
			this.updateTickRate = 1;
			this.updaterConsumer = null;
			this.interrupt = null;
			this.parent = null;
			this.inventoryArchetype = InventoryArchetypes.CHEST;
		}

		/**
		 * Puts an {@link Element} into the Page and assigns the slot
		 * it should take up in the {@link InventoryDimension}.
		 *
		 * @param slot the position in the InventoryDimension
		 * @param element the {@link Element} to be assigned
		 * @return this PageBuilder
		 */
		public PageBuilder putElement(final int slot, @Nonnull final Element element)
		{
			this.elements.put(slot, element);
			return this;
		}

		/**
		 * Adds an {@link Element} to the Page. The Element is
		 * automatically assigned after the last one.
		 *
		 * @param element the Element to be added
		 * @return this PageBuilder
		 */
		public PageBuilder addElement(@Nonnull final Element element)
		{
			this.elements.put(this.elements.size(), element);
			return this;
		}

		/**
		 * Sets the {@link InventoryDimension} to be used by this Page.
		 *
		 * @param inventoryDimension the InventoryDimension to be used
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setInventoryDimension(@Nonnull final InventoryDimension inventoryDimension)
		{
			this.inventoryDimension = inventoryDimension;
			return this;
		}

		@Nonnull
		public PageBuilder setInventoryArchetype(@Nonnull final InventoryArchetype inventoryArchetype)
		{
			this.inventoryArchetype = inventoryArchetype;
			return this;
		}

		/**
		 * Sets the title of this chest
		 *
		 * @param title the title of the chest
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setTitle(@Nonnull final Text title)
		{
			this.title = title;
			return this;
		}

		public PageBuilder setParent(@Nullable String parent)
		{
			this.parent = parent;
			return this;
		}

		/**
		 * Sets the {@link ItemStack} to be used as a default, if
		 * no {@link Element} is assigned.
		 *
		 * <p>If unset, this will default to {@link Page#defaultEmptyStack}.</p>
		 *
		 * @param emptyStack  An ItemStack that will act as the defaultEmptyStack.
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setEmptyStack(@Nonnull final ItemStack emptyStack)
		{
			this.emptyStack = emptyStack;
			return this;
		}

		@Nonnull
		public PageBuilder setBackStack(@Nonnull final ItemStack backStack)
		{
			this.backStack = backStack;
			return this;
		}

		@Nonnull
		public PageBuilder setNextStack(@Nonnull final ItemStack nextStack)
		{
			this.nextStack = nextStack;
			return this;
		}

		@Nonnull
		public PageBuilder setCloseStack(@Nonnull final ItemStack closeStack)
		{
			this.closeStack = closeStack;
			return this;
		}

		/**
		 * Sets whether or not to fill empty spaces.
		 *
		 * @param fillWhenEmpty whether or not to fill empty spaces
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setFillWhenEmpty(final boolean fillWhenEmpty)
		{
			this.fillWhenEmpty = fillWhenEmpty;
			return this;
		}

		/**
		 * Sets whether or not to let HuskyUI to handling paging.
		 *
		 * @param autoPaging whether or not HuskyUI should handle paging
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setAutoPaging(final boolean autoPaging)
		{
			this.autoPaging = autoPaging;
			return this;
		}

		/**
		 * Sets whether or not {@link ItemStack}s should be centered.
		 *
		 * @param centered whether or not ItemStacks should be centered
		 * @return this PageBuilder
		 */
		@Nonnull
		public PageBuilder setCentered(final boolean centered)
		{
			this.centered = centered;
			return this;
		}

		@Nonnull
		public PageBuilder setUpdatable(final boolean updatable)
		{
			this.updatable = updatable;
			return this;
		}

		@Nonnull
		public PageBuilder setUpdateTickRate(final int updateTickRate)
		{
			this.updateTickRate = updateTickRate;
			return this;
		}

		@Nonnull
		public PageBuilder setUpdater(final Consumer<Page> updaterConsumer)
		{
			this.updaterConsumer = updaterConsumer;
			return this;
		}

		@Nonnull
		public PageBuilder setInterrupt(final Runnable interrupt)
		{
			this.interrupt = interrupt;
			return this;
		}

		/**
		 * Builds this PageBuilder to get a new {@link Page} object.
		 *
		 * @param id the ID of the new State (Page)
		 * @return a new Page
		 */
		public Page build(@Nonnull final String id)
		{
			final int rows = (this.inventoryDimension == null) ? Math.min(5, (int) Math.ceil(((double) this.elements.size()) / 9d)) : this.inventoryDimension.getRows() - 1;
			InventoryDimension real = (this.inventoryDimension == null ? (this.autoPaging) ? InventoryDimension.of(9, rows + 1) : InventoryDimension.of(9, 4) : this.inventoryDimension);
			return new Page(id, this.elements, real, this.inventoryArchetype, this.title, this.emptyStack, this.backStack, this.nextStack, this.closeStack, this.updatable, this.updateTickRate, this.updaterConsumer, this.interrupt, this.fillWhenEmpty, this.autoPaging, this.centered, rows, parent);
		}
	}
}
