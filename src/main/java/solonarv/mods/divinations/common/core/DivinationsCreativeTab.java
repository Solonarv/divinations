package solonarv.mods.divinations.common.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import solonarv.mods.divinations.common.constants.Misc;

public class DivinationsCreativeTab extends CreativeTabs {

    public static DivinationsCreativeTab instance = new DivinationsCreativeTab();

    public DivinationsCreativeTab(){
        super(Misc.MOD_ID);
        setNoTitle();
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.ENDER_EYE);
    }
}
