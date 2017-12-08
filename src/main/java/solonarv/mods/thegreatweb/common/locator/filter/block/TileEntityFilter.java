package solonarv.mods.thegreatweb.common.locator.filter.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.lib.ConstantFactory;
import solonarv.mods.thegreatweb.common.lib.IFactoryNBT;
import solonarv.mods.thegreatweb.common.lib.util.ResourceLocationUtils;
import solonarv.mods.thegreatweb.common.locator.filter.AbstractFilter;
import solonarv.mods.thegreatweb.common.locator.result.BlockResult;

public class TileEntityFilter extends AbstractFilter<BlockResult> {

    public static final TileEntityFilter instance = new TileEntityFilter();

    public static final IFactoryNBT<TileEntityFilter> factory = new ConstantFactory<>(ResourceLocationUtils.withModID("hasTileEntity"), instance);

    @Override
    public boolean matches(World world, Vec3d position, EntityPlayer user, BlockResult candidate) {
        return candidate.getTileEntity() != null;
    }

    @Override
    public Class<BlockResult> getInputClass() {
        return BlockResult.class;
    }
}
