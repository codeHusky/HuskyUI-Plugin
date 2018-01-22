# HuskyUI
A simple, lightweight UI system for, as of right now, chest GUIs.

**[Java Docs](http://jd.codehusky.com/huskyui/)**

**[Forum Topic](https://forums.spongepowered.org/t/huskyui-a-simple-fast-ui-system-for-plugins/19557/4)**

**[HuskyUI Wiki](https://github.com/codeHusky/HuskyUI-Plugin/wiki)**

# Implementation Examples

### Hotbar Compass using ElementRegistry

```java
RunnableAction testAction = new RunnableAction(registry, ActionType.NONE,"");
testAction.setRunnable(context -> {
    StateContainer container = new StateContainer();
    Page testPage = Page.builder()
            .setTitle(Text.of(TextColors.GOLD,"Navigator"))
            .setAutoPaging(true)
            .addElement(new Element(
                    ItemStack.builder()
                        .itemType(ItemTypes.DIAMOND)
                        .add(Keys.DISPLAY_NAME,Text.of(TextColors.BLUE,"Diamond Rush"))
                        .build()
                    ))
            .addElement(new Element(
                    ItemStack.builder()
                            .itemType(ItemTypes.FIREWORKS)
                            .add(Keys.DISPLAY_NAME,Text.of(TextColors.RED,"Fireworks Palooza"))
                            .build()
            ))
            .addElement(new Element(
                    ItemStack.builder()
                            .itemType(ItemTypes.MINECART)
                            .add(Keys.DISPLAY_NAME,Text.of(TextColors.GRAY,"Roller Coasters"))
                            .build()
            ))
            .build("testpage");
    container.setInitialState(testPage);
    container.launchFor(context.getObserver());
});
ActionableElement testElement = new ActionableElement(
                                    testAction,
                                    ItemStack.builder()
                                            .itemType(ItemTypes.COMPASS)
                                            .add(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD,"Navigator"))
                                            .build());

HuskyUI.getElementRegistry().registerAutoElement(4,testElement);
ItemStack litMC = ItemStack.builder()
        .itemType(ItemTypes.REDSTONE_TORCH)
        .add(Keys.DISPLAY_NAME,Text.of(TextColors.RED,"LitMC"))
        .build();
HuskyUI.getElementRegistry().registerAutoElement(0,new Element(litMC));
HuskyUI.getElementRegistry().registerAutoElement(8,new Element(litMC));

HuskyUI.getElementRegistry().registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 1")).build()));
HuskyUI.getElementRegistry().registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 2")).build()));
HuskyUI.getElementRegistry().registerAutoElement(new Element(ItemStack.builder().itemType(ItemTypes.MINECART).add(Keys.DISPLAY_NAME,Text.of("movable 3")).build()));
```
**Result** (with some movement of the movable carts.)

![img](https://i.imgur.com/DvCy36o.png)

### Generic Example
```java
StateContainer container = new StateContainer();
container.addState(
    Page.builder()
        .setUpdatable(true)
        .setUpdater(page -> {
            int count = 0;
            for(Inventory slot: page.getPageView().slots()){

                if(!slot.peek().isPresent() && count == page.getTicks()%page.getPageView().capacity()){
                    slot.set(ItemStack.of(ItemTypes.STAINED_GLASS_PANE,count));
                }else{
                    if(slot.peek().isPresent()) {
                        ItemStack stack = slot.peek().get();
                        if (stack.getType() == ItemTypes.STAINED_GLASS_PANE) {
                            slot.set(ItemStack.empty());
                        }
                    }
                }
                count++;
            }
        })
        .setUpdateTickRate(20)
        .setTitle(Text.of(TextColors.RED,"BLARG"))
            .addElement(new ActionableElement(new Action(container,ActionType.NORMAL,"test2"),ItemStack.builder().
                    itemType(ItemTypes.COOKIE)
                    .build()))
        .build("test")
);
container.addState(
    Page.builder()
        .setUpdatable(false)
        .setTitle(Text.of(TextColors.GREEN,"alt"))
        .addElement(new ActionableElement(new Action(container,ActionType.BACK,"test"),ItemStack.builder().
                itemType(ItemTypes.COOKIE)
                .build()))
        .setParent("test")
        .build("test2")
);
container.launchFor(plr);
```
