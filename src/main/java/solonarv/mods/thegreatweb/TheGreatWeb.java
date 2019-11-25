package solonarv.mods.thegreatweb;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import solonarv.mods.thegreatweb.item.base.ModItems;

public class TheGreatWeb implements ModInitializer {
    private static Logger logger;

    public static Logger logger(){
        return logger;
    }

    @Override
    public void onInitialize() {
        logger= LogManager.getLogger();
        ModItems.registerItems();
    }
}
