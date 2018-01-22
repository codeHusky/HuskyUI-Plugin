package com.codehusky.huskyui;

import com.codehusky.huskyui.states.element.ActionableElement;
import com.codehusky.huskyui.states.element.Element;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * ElementRegistry is a class only meant for use inside of HuskyUI to register elements with the plugin.
 * Using it for any other purpose is not advised.
 * This is for messing with Elements within player inventories.
 * <br><br>
 * Auto-Inv and methods referencing Auto are talking about Auto-Inventory Delivery of the element.
 * Elements delivered to player inventories will be placed in the first available slot unless specified otherwise.
 * <br><br>
 * <span style="color:red"><h2>!! WARNING !!</h2></span>
 * <b>Enforcing a slot id for an auto element will result on player's inventories being cleared every time they log in.</b><br>
 * <b>Please use this feature with care! By default, you should not do this. Make it controlled via config (default off) or be very explicit in disclosure of this feature.</b><br>
 * <b>Improper warning of this feature could be disastrous for some servers.</b><br>
 *
 */
public class ElementRegistry extends StateContainer {

    private HashMap<Integer, Element> elements;

    //position, element id
    private HashMap<Integer, Integer> autoInvLocations;
    private ArrayList<Integer> autoInvElements;

    private int elementIncrement = 0;

    /**
     * Initialization for Registry.
     * For use only by the HuskyUI main class.
     */
    ElementRegistry() {
        elements = new HashMap<>();
        autoInvLocations = new HashMap<>();
        autoInvElements = new ArrayList<>();
    }


    /**
     * Register an element to HuskyUI's Element Registry.
     *
     * @param element Element to register
     * @return elementID of registered element
     */
    public int registerElement(Element element){
        int key = elementIncrement;
        elements.put(key,element);
        elementIncrement++;
        return key;
    }

    /**
     * Unregister an element from the Registry.<br>
     * Note that this will not remove an item from a player registered to this ID, so use of this method is heavily discouraged.
     *
     * @param id Element to unregister
     */
    public void unregisterElement(int id){
        if(elements.containsKey(id)){
            elements.remove(id);
            if(autoInvElements.contains(id)){
                autoInvElements.remove(id);
                if(autoInvLocations.values().contains(id)){
                    autoInvLocations.values().removeIf(val -> id == val);
                }
            }
        }else{
            throw new RuntimeException("Cannot unregister element: Element id \"" + id + "\" is not registered.");
        }
    }

    /**
     * Register an element to the Registry and also the auto-deployment system.<br>
     * Items registered in this way will be given to the player when they log into the server.
     *
     * @param element Element to register
     * @return elementID of registered element.
     */
    public int registerAutoElement(Element element){
        int id = registerElement(element);
        autoInvElements.add(id);
        return id;
    }

    /**
     * Register an existing element to the ElementRegistry<br>
     * @param elementID ElementID to register with auto-inv delivery enabled
     */
    public void registerAutoElement(int elementID){
        if(!elements.containsKey(elementID)){
            throw new RuntimeException("Cannot register element as auto: Element id \"" + elementID + "\" is not registered.");
        }
        if(autoInvElements.contains(elementID)) return;
        autoInvElements.add(elementID);
    }

    /**
     * Registers an element at a given fixed slot position.<br><br>
     * <b>WARNING: Adding anything to a fixed slot ID will result in the removal of all user items on their spawn.</b><br>
     * <i>Please make use of this method configurable in your plugin, with it being disabled by default. Thanks!</i>
     *
     * @param slotID SlotID to associate element with
     * @param element Element to register
     * @return ElementID
     */
    public int registerAutoElement(int slotID, Element element){
        int id = registerElement(element);
        autoInvLocations.put(slotID,id);
        autoInvElements.add(id);
        return id;
    }

    /**
     * Associates an existing element to a given fixed slot position.<br><br>
     * <b>WARNING: Adding anything to a fixed slot ID will result in the removal of all user items on their spawn.</b><br>
     * <i>Please make use of this method configurable in your plugin, with it being disabled by default. Thanks!</i>
     *
     * @param slotID SlotID to associate element with
     * @param elementID ElementID to bind to slot.
     */
    public void registerAutoElement(int slotID, int elementID){
        if(!elements.containsKey(elementID)){
            throw new RuntimeException("Cannot register element as auto: Element id \"" + elementID + "\" is not registered.");
        }
        autoInvLocations.put(slotID,elementID);

        if(!autoInvElements.contains(elementID))
            autoInvElements.add(elementID);
    }

    /**
     * Get all auto-given items (placed in user inventory on login)
     * @return HashMap of ElementIDs to Element itemstacks.
     */
    public HashMap<Integer,ItemStack> getAutoItems() {
        HashMap<Integer,ItemStack> stacks = new HashMap<>();
        for(int i : autoInvElements){
            stacks.put(i,getItemStackForElement(i));
        }
        return stacks;
    }

    /**
     * Get all fixed locations of auto items.<br>
     * If this hashmap is empty, inventories are NOT reset on login.
     *
     * @return HashMap containing SlotIDs to ElementIDs
     */
    public HashMap<Integer,Integer> getAutoItemLocations() {
        return autoInvLocations;
    }

    /**
     * Convert an ItemStack into an Element
     *
     * @param stack ItemStack to pull Element from
     * @return Element, if it exists with that item.
     */
    public Optional<Element> getElementFromItemStack(ItemStack stack){
        Optional<Integer> optID = getElementIDFromItemStack(stack);
        if(optID.isPresent()){
            if(elements.containsKey(optID.get())){
                return Optional.of(elements.get(optID.get()));
            }
        }
        return Optional.empty();
    }

    /**
     * Convert an ItemStack into an ElementID
     *
     * @param stack ItemStack to pull ElementID from
     * @return ElementID, if it exists. This ID may not actually be associated with an Element, so please verify that before use with {@link #elementExists(int)}.
     */
    public Optional<Integer> getElementIDFromItemStack(ItemStack stack){
        if(stack.getType() == ItemTypes.AIR || stack.getType() == ItemTypes.NONE) return Optional.empty();
        Optional<Object> optRegID = stack.toContainer().get(DataQuery.of("UnsafeData", "regid"));
        if(optRegID.isPresent()){
            return Optional.of((int)optRegID.get());
        }
        return Optional.empty();
    }

    /**
     * Verify that an ElementID actually is registered to an Element
     *
     * @param id ElementID to check against
     * @return If element exists under that id
     */
    public boolean elementExists(int id){
        return elements.containsKey(id);
    }

    /**
     * Get an inventory-ready itemstack for any given element
     *
     * @param elementID ElementID to get an itemstack of.
     * @return ItemStack of given element.
     */
    public ItemStack getItemStackForElement(int elementID){
        if(elements.containsKey(elementID)){
            return ItemStack.builder()
                    .fromContainer(elements.get(elementID)
                            .getItem()
                            .toContainer()
                            .set(DataQuery.of("UnsafeData", "regid"), elementID))
                    .build();
        }
        throw new RuntimeException("Cannot get ItemStack: Element id \"" + elementID + "\" is not registered.");
    }
}
