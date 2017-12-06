package solonarv.mods.thegreatweb.common.item.base;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.block.ModBlocks;
import solonarv.mods.thegreatweb.common.core.TheGreatWebCreativeTab;
import solonarv.mods.thegreatweb.common.item.ItemBlockLocator;

@Mod.EventBusSubscriber
public class ModItems {
    // public static final ItemPigFinder pigFinder = new ItemPigFinder();
    public static final ItemBlockLocator blockLocator = new ItemBlockLocator();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        TheGreatWeb.logger.info("Registering items!");
        event.getRegistry().registerAll(blockLocator);
        for (Block blk : ModBlocks.allBlocks) {
            event.getRegistry().register(new ItemBlock(blk).setRegistryName(blk.getRegistryName()).setCreativeTab(TheGreatWebCreativeTab.instance));
        }
    }

    public static void initModels() {
        blockLocator.initModel();
    }
}
