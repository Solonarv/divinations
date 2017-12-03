package solonarv.mods.thegreatweb.common.locator.filter.block;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.lib.IFactoryNBT;
import solonarv.mods.thegreatweb.common.lib.Util;
import solonarv.mods.thegreatweb.common.locator.filter.AbstractFilter;
import solonarv.mods.thegreatweb.common.locator.filter.IFilter;
import solonarv.mods.thegreatweb.common.locator.result.BlockResult;

public class LightLevelFilter extends AbstractFilter<BlockResult> {
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

    @Override
    public Class<BlockResult> getInputClass() {
        return BlockResult.class;
    }

    public static final ResourceLocation id = Util.resourceLocationWithDefaultDomain("lightLevel");

    public static final IFactoryNBT<LightLevelFilter> factory = new IFactoryNBT<LightLevelFilter>() {
        @Override
        public LightLevelFilter readNBT(NBTTagCompound tag) {
            byte minLight = tag.getByte("minLight");
            byte maxLight = tag.getByte("maxLight");
            boolean isSky = tag.getBoolean("isSky");
            return new LightLevelFilter(minLight, maxLight, isSky ? EnumSkyBlock.SKY : EnumSkyBlock.BLOCK);
        }

        @Override
        public ResourceLocation getResourceName() {
            return id;
        }
    };
}
