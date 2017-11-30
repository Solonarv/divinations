package solonarv.mods.divinations.common.core;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.item.base.ModItems;
import solonarv.mods.divinations.common.locator.consumer.Consumers;
import solonarv.mods.divinations.common.locator.filter.Filters;
import solonarv.mods.divinations.common.locator.selector.Selectors;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Filters.init();
        Selectors.init();
        Consumers.init();
        ModItems.preInit();
    }
}
