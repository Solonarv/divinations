package solonarv.mods.divinations.common.core;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.item.base.ModItems;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ModItems.preInit();
    }
}
