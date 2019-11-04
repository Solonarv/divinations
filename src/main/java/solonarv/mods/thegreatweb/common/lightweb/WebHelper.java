package solonarv.mods.thegreatweb.common.lightweb;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WebHelper {
    public static double getLeyWeightForBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        double explosionResistance = block.getExplosionResistance(world, pos, null, null);

        return explosionResistance == 0 ? 0 : Math.log(1 + 1/explosionResistance);
    }
}
