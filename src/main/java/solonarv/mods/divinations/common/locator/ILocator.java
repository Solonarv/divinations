package solonarv.mods.divinations.common.locator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public interface ILocator {
    List<LocatorResult> getResults(World world, EntityPlayer owner, Vec3d position);
}
