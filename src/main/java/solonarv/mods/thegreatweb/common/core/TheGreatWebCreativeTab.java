package solonarv.mods.thegreatweb.common.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import solonarv.mods.thegreatweb.common.constants.Misc;

public class TheGreatWebCreativeTab extends CreativeTabs {

    public static TheGreatWebCreativeTab instance = new TheGreatWebCreativeTab();

    public TheGreatWebCreativeTab(){
        super(Misc.MOD_ID);
        setNoTitle();
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.ENDER_EYE);
    }
}
