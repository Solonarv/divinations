package solonarv.mods.thegreatweb.client.core;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import solonarv.mods.thegreatweb.common.block.ModBlocks;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.core.CommonProxy;
import solonarv.mods.thegreatweb.common.item.base.ModItems;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Misc.MOD_ID)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.initModels();
        ModBlocks.initModels();
    }

    @Override
    public void registerEventHandlers() {
        super.registerEventHandlers();
    }
}
