package solonarv.mods.divinations.common.locator.filter.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.lib.IFactoryNBT;
import solonarv.mods.divinations.common.locator.filter.IFilter;
import solonarv.mods.divinations.common.locator.result.BlockResult;

public class LightLevelFilter implements IFilter<BlockResult> {
    private final byte minLight;
    private final byte maxLight;
    private final boolean reversed;
    private final EnumSkyBlock skyOrBlock;

    public LightLevelFilter(byte minLight, byte maxLight, EnumSkyBlock skyOrBlock) {
        this.skyOrBlock = skyOrBlock;
        this.reversed = minLight > maxLight;
        this.minLight = reversed ? maxLight : minLight;
        this.maxLight = reversed ? minLight : maxLight;
    }

    @Override
    public boolean matches(World world, Vec3d position, EntityPlayer user, BlockResult candidate) {
        int lightLevel = world.getLightFor(skyOrBlock, candidate.getBlockPos());

        boolean inRange = lightLevel <= maxLight && lightLevel >= minLight;

        return inRange ^ reversed;
    }

    public static final IFactoryNBT<LightLevelFilter> factory = tag -> {
        byte minLight = tag.getByte("minLight");
        byte maxLight = tag.getByte("maxLight");
        boolean isSky = tag.getBoolean("isSky");
        return new LightLevelFilter(minLight, maxLight, isSky ? EnumSkyBlock.SKY : EnumSkyBlock.BLOCK);
    };
}
