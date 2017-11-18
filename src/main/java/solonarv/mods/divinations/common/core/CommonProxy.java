package solonarv.mods.divinations.common.core;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.item.base.ModItems;
import solonarv.mods.divinations.common.locator.consumer.Consumers;
import solonarv.mods.divinations.common.locator.filter.Filters;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Consumers.init();
        Filters.init();
        ModItems.preInit();
    }
}
