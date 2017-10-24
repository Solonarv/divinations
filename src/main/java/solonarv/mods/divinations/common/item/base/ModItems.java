package solonarv.mods.divinations.common.item.base;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import solonarv.mods.divinations.common.item.ItemPigFinder;

@Mod.EventBusSubscriber
public class ModItems {
    public static ItemPigFinder pigFinder;

    public static void preInit(){
        pigFinder = new ItemPigFinder();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        System.out.println("Registering items!");
        if (pigFinder == null) {
            System.err.println("pig_finder is null; was PreInit skipped???");
        }
        event.getRegistry().registerAll(pigFinder);
    }
}
