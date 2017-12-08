package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.util.ResourceLocation;
import solonarv.mods.thegreatweb.common.constants.Misc;

public class ResourceLocationUtils {
    public static ResourceLocation resourceLocationWithDefaultDomain(String s){
        String[] split = s.split(":", 2);
        if (split.length < 2) {
            split = new String[]{Misc.MOD_ID, s};
        }
        return new ResourceLocation(split[0], split[1]);
    }

    public static ResourceLocation withModID(String name) {
        return new ResourceLocation(Misc.MOD_ID, name);
    }
}
