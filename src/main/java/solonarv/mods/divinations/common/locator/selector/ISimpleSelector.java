package solonarv.mods.divinations.common.locator.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.List;

public interface ISimpleSelector<T extends ILocatorResult> {
    List<T> select(World world, Vec3d position, EntityPlayer user, List<T> candidate);
}
