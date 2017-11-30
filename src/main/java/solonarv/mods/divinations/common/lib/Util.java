package solonarv.mods.divinations.common.lib;

import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.constants.Misc;

public class Util {
    public static ResourceLocation resourceLocationWithDefaultDomain(String s){
        System.out.println("Generating MODID ResourceLocation for string " + s);
        String[] split = s.split(":", 2);
        if (split.length < 2) {
            split = new String[]{Misc.MOD_ID, s};
            System.out.println("No prefix found, substituting MODOD (" + Misc.MOD_ID + ")");
        }
        return new ResourceLocation(split[0], split[1]);
    }

    public static ResourceLocation withModID(String name) {
        return new ResourceLocation(Misc.MOD_ID, name);
    }
}
