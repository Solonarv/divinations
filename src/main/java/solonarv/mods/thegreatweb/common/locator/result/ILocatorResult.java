package solonarv.mods.thegreatweb.common.locator.result;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface ILocatorResult {
    World getWorld();
    Vec3d getResultLocation();
}
