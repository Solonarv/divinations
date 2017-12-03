package solonarv.mods.thegreatweb.common.item.base;

import net.minecraft.item.Item;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.core.TheGreatWebCreativeTab;

public class ItemMod extends Item {
    public ItemMod(String name) {
        super();
        setCreativeTab(TheGreatWebCreativeTab.instance);
        setUnlocalizedName(name);
        setRegistryName(Misc.MOD_ID, name);
    }
}
