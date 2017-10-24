package solonarv.mods.divinations.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import solonarv.mods.divinations.common.core.CommonProxy;
import solonarv.mods.divinations.common.constants.Misc;

@Mod(modid = Misc.MOD_ID, version = Misc.VERSION)
public class Divinations
{

    @Mod.Instance(Misc.MOD_ID)
    public static Divinations instance;

    @SidedProxy(serverSide = Misc.PROXY_COMMON, clientSide =  Misc.PROXY_CLIENT)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        System.out.println("Divinations mod loading!");
        proxy.preInit(event);
    }
}
