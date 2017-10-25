package solonarv.mods.divinations.common.locator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public abstract class GenericBlockLocator implements IConcreteLocator {

    private static final int MAX_RANGE = 32;
    public final int range;

    public GenericBlockLocator(){
        this(MAX_RANGE);
    }

    public GenericBlockLocator(int range) {
        this.range = range > MAX_RANGE ? MAX_RANGE : range;
    }

    public abstract boolean testBlock(World world, net.minecraft.util.math.BlockPos pos);

    @Override
    @Nonnull
    public List<LocatorResult> getResults(World world, EntityPlayer owner, Vec3d position) {
        List<LocatorResult> results = new LinkedList<>();
        net.minecraft.util.math.BlockPos center = new net.minecraft.util.math.BlockPos(position);
        net.minecraft.util.math.BlockPos minCorner = center.subtract(new Vec3i(range, range, range));
        net.minecraft.util.math.BlockPos maxCorner = center.add(new Vec3i(range, range, range));

        for (net.minecraft.util.math.BlockPos cur : net.minecraft.util.math.BlockPos.getAllInBoxMutable(minCorner, maxCorner)) {
            if (testBlock(world, cur)) {
                results.add(new LocatorResult.BlockPos(cur.toImmutable()));
            }
        }

        return results;
    }
}
