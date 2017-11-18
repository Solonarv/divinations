package solonarv.mods.divinations.common.locator.consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.List;

public interface IConsumer<T extends ILocatorResult> {
    void useResults(World world, Vec3d position, EntityPlayer user, List<T> results);
}
