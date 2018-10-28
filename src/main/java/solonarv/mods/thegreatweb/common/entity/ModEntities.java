package solonarv.mods.thegreatweb.common.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.lib.util.ResourceLocationUtils;

public class ModEntities {

    public static void init() {
        registerEntity("leyNode", EntityLeyNode.class, 128, 5, true);
    }

    private static int nextID = 1;
    private static void registerEntity(String etyName, Class<? extends Entity> etyClass, int trackingRange, int updateFrequency, boolean sendVelocityUpdates) {
        EntityRegistry.registerModEntity(ResourceLocationUtils.withModID(etyName), etyClass, etyName, nextID++, TheGreatWeb.instance, trackingRange, updateFrequency, sendVelocityUpdates);
    }
}
