package solonarv.mods.thegreatweb.common.core;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.locator.consumer.Consumers;
import solonarv.mods.thegreatweb.common.locator.filter.Filters;
import solonarv.mods.thegreatweb.common.locator.selector.Selectors;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Filters.init();
        Selectors.init();
        Consumers.init();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(TheGreatWeb.instance, new GuiProxy());
    }
}
