package solonarv.mods.thegreatweb.common.block;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import solonarv.mods.thegreatweb.common.locator.BlockLocator;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class ModBlocks {
    public static final BlockLocatorWorkbench locatorWorkbench = new BlockLocatorWorkbench();

    public static final Block[] allBlocks = new Block[] {locatorWorkbench};

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        System.out.println("Registering blocks!");
        event.getRegistry().registerAll(locatorWorkbench);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        locatorWorkbench.initModel();
    }
}
