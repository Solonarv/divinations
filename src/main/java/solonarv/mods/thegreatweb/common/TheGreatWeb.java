package solonarv.mods.thegreatweb.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.thegreatweb.common.core.CommonProxy;
import solonarv.mods.thegreatweb.common.constants.Misc;

@Mod(modid = Misc.MOD_ID, version = Misc.VERSION)
public class TheGreatWeb
{

    @Mod.Instance(Misc.MOD_ID)
    public static TheGreatWeb instance;

    @SidedProxy(serverSide = Misc.PROXY_COMMON, clientSide =  Misc.PROXY_CLIENT)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}