package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import solonarv.mods.thegreatweb.common.entity.EntityLeyNode;

public class EntityHelper {
    public static Vec3d getEntityMotion(Entity entity) {
        return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
    }
}
