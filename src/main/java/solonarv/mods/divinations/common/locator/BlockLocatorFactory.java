package solonarv.mods.divinations.common.locator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import solonarv.mods.divinations.common.locator.base.BaseLocator;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;

import javax.annotation.Nonnull;

public class BlockLocatorFactory extends BaseLocator {
    @Override
    public IConcreteLocator fromNBT(@Nonnull NBTTagCompound tag) {

        if (!tag.hasKey("block", Constants.NBT.TAG_COMPOUND))
            return null;
        NBTTagCompound blockData = tag.getCompoundTag("block");
        String blockID = blockData.getString("id");
        byte meta = blockData.hasKey("meta", Constants.NBT.TAG_BYTE) ? blockData.getByte("meta") : (byte)255;
        Block block = Block.getBlockFromName(blockID);

        return new BlockIDMetaLocator(block, meta);
    }

    static class BlockIDMetaLocator extends GenericBlockLocator {

        private final Block block;
        private final byte meta;

        BlockIDMetaLocator(Block block, byte meta) {
            this.block = block;
            this.meta = meta;
        }

        @Override
        public boolean testBlock(World world, BlockPos pos) {
            IBlockState theBlock = world.getBlockState(pos);
            return theBlock.getBlock() == block && (meta == (byte)255 ||block.getMetaFromState(theBlock) == meta);
        }
    }
}
