package solonarv.mods.thegreatweb.common.locator;

import com.google.common.collect.Iterables;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.locator.result.BlockResult;

public class BlockLocator extends BaseLocator<BlockResult> {

    public static final int MAX_RADIUS = 32;
    private static final BlockPos OFFSET = new BlockPos(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS);
    private static final int CUBE_WIDTH = MAX_RADIUS * 2 + 1;

    private static final BlockLocator instance = new BlockLocator();

    public static BlockLocator getInstance() {
        return instance;
    }

    @Override
    Iterable<BlockResult> findResultsRaw(World world, Vec3d position, EntityPlayer user) {
        BlockPos center = new BlockPos(position);
        BlockPos minCorner = center.subtract(OFFSET);
        BlockPos maxCorner = center.add(OFFSET);

        return Iterables.transform(BlockPos.getAllInBox(minCorner, maxCorner), bp -> new BlockResult(world, bp));
    }

    @Override
    public Class getInputClass() {
        return BlockResult.class;
    }
}
