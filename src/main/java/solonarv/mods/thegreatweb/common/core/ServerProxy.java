package solonarv.mods.thegreatweb.common.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.thegreatweb.common.network.LeyWebSync;

public class ServerProxy extends CommonProxy {

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();

        MinecraftForge.EVENT_BUS.register(LeyWebSync.class);
    }
}
