package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

public class NBTHelper {
    @Nonnull
    public static Vec3d getVec3d(NBTTagCompound compound, String keyTemplate) {
        return new Vec3d(
                  compound.getDouble(keyTemplate + "_X")
                , compound.getDouble(keyTemplate + "_Y")
                , compound.getDouble(keyTemplate + "_Z"));
    }

    public static void writeVec3d(NBTTagCompound compound, String keyTemplate, Vec3d vec3d) {
        compound.setDouble(keyTemplate + "_X", vec3d.x);
        compound.setDouble(keyTemplate + "_Y", vec3d.y);
        compound.setDouble(keyTemplate + "_Z", vec3d.z);
    }
}
