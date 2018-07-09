package net.aufdemrand.denizen.objects.properties.item;

import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dMaterial;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.nbt.CustomNBT;
import net.aufdemrand.denizencore.objects.Mechanism;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.objects.properties.Property;
import net.aufdemrand.denizencore.tags.Attribute;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemCanPlaceOn implements Property {

    public static boolean describes(dObject item) {
        return item instanceof dItem;
    }

    public static ItemCanPlaceOn getFrom(dObject item) {
        if (!describes(item)) {
            return null;
        }
        else {
            return new ItemCanPlaceOn((dItem) item);
        }
    }

    private ItemCanPlaceOn(dItem item) {
        this.item = item;
    }

    dItem item;

    private dList getMaterials() {
        ItemStack itemStack = item.getItemStack();
        List<Material> materials = CustomNBT.getNBTMaterials(itemStack, CustomNBT.KEY_CAN_PLACE_ON);
        if (materials != null && !materials.isEmpty()) {
            dList list = new dList();
            for (Material material : materials) {
                list.addObject(dMaterial.getMaterialFrom(material));
            }
            return list;
        }
        return null;
    }

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <i@item.can_place_on>
        // @returns dList(dMaterial)
        // @group properties
        // @mechanism dItem.can_place_on
        // @description
        // Returns a list of materials this item can be placed on while in adventure mode, if any.
        // -->
        if (attribute.startsWith("can_place_on")) {
            dList materials = getMaterials();
            if (materials != null) {
                return materials.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }

    @Override
    public String getPropertyString() {
        dList materials = getMaterials();
        return materials != null ? materials.identify() : null;
    }

    @Override
    public String getPropertyId() {
        return "can_place_on";
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object dItem
        // @name can_place_on
        // @input dList(dMaterial)
        // @description
        // Sets the materials this item can be placed on while in adventure mode.
        // @tags
        // <i@item.can_place_on>
        // -->
        if (mechanism.matches("can_place_on")) {
            if (item.getMaterial().getMaterial() == Material.AIR) {
                dB.echoError("Cannot apply NBT to AIR!");
                return;
            }
            List<dMaterial> materials = mechanism.getValue().asType(dList.class).filter(dMaterial.class);
            ItemStack itemStack = item.getItemStack();
            itemStack = CustomNBT.clearNBT(itemStack, CustomNBT.KEY_CAN_PLACE_ON);
            for (dMaterial material : materials) {
                itemStack = CustomNBT.addNBTMaterial(itemStack, CustomNBT.KEY_CAN_PLACE_ON, material.getMaterial());
            }
            item.setItemStack(itemStack);
        }
    }
}
