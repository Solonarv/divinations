package solonarv.mods.divinations.common.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.item.base.ModItems;
import solonarv.mods.divinations.common.locator.Locators;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;
import solonarv.mods.divinations.common.locator.base.LocatorResult;
import solonarv.mods.divinations.common.resultdisplay.IResultDisplay;
import solonarv.mods.divinations.common.selector.base.ISelector;

import java.util.List;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Locators.registerDefaultLocators();
        ModItems.preInit();
    }

    public void performLocate(World worldIn, EntityPlayer playerIn, Vec3d pos, IConcreteLocator locator, ISelector selector, IResultDisplay resultDisplay) {
        List<LocatorResult> rawResults = locator.getResults(worldIn, playerIn, pos);
        for (LocatorResult result : selector.makeSelection(playerIn, pos, rawResults)) {
            resultDisplay.displayResults(playerIn, pos, result);
        }
    }
}
