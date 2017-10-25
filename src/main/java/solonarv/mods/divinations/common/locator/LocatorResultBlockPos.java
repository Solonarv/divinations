package solonarv.mods.divinations.common.locator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class LocatorResultBlockPos extends LocatorResult {


    private final BlockPos position;

    protected LocatorResultBlockPos(BlockPos position) {
        super(LocatorResultType.BLOCK);
        this.position = position;
    }

    @Override
    public Vec3d getPosition() {
        return new Vec3d(position.getX(), position.getY(), position.getZ());
    }

    public BlockPos getBlockPos() {
        return this.position;
    }
}
