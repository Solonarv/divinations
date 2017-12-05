package solonarv.mods.thegreatweb.common.leyweb;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class LeyNode extends Vec3i {

    public LeyNode(int xIn, int yIn, int zIn) {
        super(xIn, yIn, zIn);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(this);
    }

}
