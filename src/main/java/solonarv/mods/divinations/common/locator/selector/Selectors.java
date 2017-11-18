package solonarv.mods.divinations.common.locator.selector;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Selectors {
    private static Map<ResourceLocation, ISelector> selectors = new HashMap<>();
    private static boolean initDone = false;

    public static void init() {
        if (initDone)
            return;

        initDone = true;
    }
}
