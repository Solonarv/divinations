package solonarv.mods.divinations.common.locator.result;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

public class BlockResult implements ILocatorResult {

    private final World world;
    private final BlockPos location;
    // This is cached so we don't need to look it up multiple times if there are many filters.
    private final IBlockState blockState;
    private final TileEntity tileEntity;

    public BlockResult(World world, BlockPos location, IBlockState blockState, TileEntity tileEntity) {
        this.world = world;
        this.location = location;
        this.blockState = blockState;
        this.tileEntity = tileEntity;
    }

    public BlockResult(World world, BlockPos location) {
        this(world, location, world.getBlockState(location), world.getTileEntity(location));
    }

    public World getWorld() {
        return world;
    }

    public IBlockState getBlockState() {
        return blockState;
    }

    @Nullable
    public TileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public Vec3d getResultLocation() {
        return new Vec3d(location);
    }


    public BlockPos getBlockPos() {
        return location;
    }

    @Override
    public String toString(){
        return blockState.toString() + " at xyz=[" + location.getX() + ", " + location.getY() + ", " + location.getZ() + "].";
    }
}
