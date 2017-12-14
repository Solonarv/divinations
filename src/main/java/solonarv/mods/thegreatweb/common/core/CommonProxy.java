package solonarv.mods.thegreatweb.common.core;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.locator.consumer.Consumers;
import solonarv.mods.thegreatweb.common.locator.filter.Filters;
import solonarv.mods.thegreatweb.common.locator.selector.Selectors;
import solonarv.mods.thegreatweb.common.network.PacketHandler;

public abstract class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        Filters.init();
        Selectors.init();
        Consumers.init();
        PacketHandler.register(Misc.MOD_ID);

        registerEventHandlers();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(TheGreatWeb.instance, new GuiProxy());
    }

    public  void registerEventHandlers() {}
}
