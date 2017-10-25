package solonarv.mods.divinations.common.item.base;

import net.minecraft.item.Item;
import solonarv.mods.divinations.common.constants.Misc;
import solonarv.mods.divinations.common.core.DivinationsCreativeTab;

public class ItemMod extends Item {
    public ItemMod(String name) {
        super();
        setCreativeTab(DivinationsCreativeTab.instance);
        setUnlocalizedName(name);
        setRegistryName(Misc.MOD_ID, name);
    }
}
