package solonarv.mods.thegreatweb.common.leyweb;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class LeyNode extends Vec3i {

    public final int id;

    public LeyNode(int xIn, int zIn, int id) {
        super(xIn, 0, zIn);
        this.id = id;
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this);
    }

    @Override
    public String toString() {
        return String.format("LeyNode[x=%d, z=%d, id=%d]", getX(), getZ(), id);
    }
}
