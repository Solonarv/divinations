package solonarv.mods.divinations.common.locator.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

public interface ISimpleFilter<T extends ILocatorResult> {
    boolean matches(World world, Vec3d position, EntityPlayer user, T candidate);
}
