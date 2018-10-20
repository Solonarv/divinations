package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MathUtil {
    public static Vec3i geometricCenter(Vec3i... vectors) {
        Vec3d result = Vec3d.ZERO;
        for (Vec3i vector : vectors) {
            result = result.addVector(vector.getX(), vector.getY(), vector.getZ());
        }
        result = result.scale(1 / (double)vectors.length);
        return new Vec3i(result.x, result.y, result.z);
    }

    public static int floor(double v) {
        return (int) Math.floor(v);
    }

    public static int nextLowestPowerOf2(int x) {
        return (int) Math.pow(2, Math.floor(MathHelper.log2(x)));
    }
}
