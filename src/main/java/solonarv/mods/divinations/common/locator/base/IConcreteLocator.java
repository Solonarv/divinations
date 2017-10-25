package solonarv.mods.divinations.common.locator.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import javax.annotation.Nonnull;
import java.util.List;

public interface IConcreteLocator {
    @Nonnull
    List<LocatorResult> getResults(World world, EntityPlayer owner, Vec3d position);
}
