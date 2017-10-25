package solonarv.mods.divinations.common.core;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.item.base.ModItems;
import solonarv.mods.divinations.common.locator.Locators;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Locators.registerDefaultLocators();
        ModItems.preInit();
    }
}
