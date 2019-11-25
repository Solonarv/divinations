package solonarv.mods.thegreatweb.lib.util;

import net.minecraft.util.Identifier;
import solonarv.mods.thegreatweb.constants.Misc;

public class IdentifierUtils {
    private IdentifierUtils(){}
    public static Identifier withDefaultDomain(String s){
        String[] split = s.split(":", 2);
        if (split.length < 2) {
            return new Identifier(Misc.MOD_ID, s);
        }
        return new Identifier(split[0],split[1]);
    }

    public static Identifier withModID(String name) {
        return new Identifier(Misc.MOD_ID, name);
    }
}
