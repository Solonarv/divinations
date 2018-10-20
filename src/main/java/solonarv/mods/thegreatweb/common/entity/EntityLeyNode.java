package solonarv.mods.thegreatweb.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebHelper;
import solonarv.mods.thegreatweb.common.lib.util.MathUtil;

import java.util.Arrays;

public class EntityLeyNode extends Entity {

    private static final String TAG_SIZE = "size";
    private int size = 0;

    private static final String TAG_NEXTINDEX = "nextIndex";
    private int nextIndex = 0;

    private int blockX, blockZ;

    private static final String TAG_MAPWEIGHTS = "mapWeights";
    private float[] weightForColumn = new float[BLOCK_INFLUENCE_DIAMETER];
    private static final String TAG_MAPELEVATIONS = "mapElevations";
    private float[] elevationForColumn = new float[BLOCK_INFLUENCE_DIAMETER];

    public static int BLOCK_INFLUENCE_RADIUS = 48;
    public static int BLOCK_INFLUENCE_DIAMETER = 2 * BLOCK_INFLUENCE_RADIUS + 1;
    public static int BLOCKS_IN_RADIUS = BLOCK_INFLUENCE_DIAMETER * BLOCK_INFLUENCE_DIAMETER;
    public static int CHECK_INDEX_STEP = MathUtil.nextLowestPowerOf2(BLOCKS_IN_RADIUS);

    public static final double BLOCK_ATTRACT_FACTOR = 1;

    public EntityLeyNode(World worldIn) {
        super(worldIn);
        isImmuneToFire = true;
    }

    @Override
    protected void entityInit() {
        setSize(0.1F, 0.5F);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        size = compound.getInteger(TAG_SIZE);
        nextIndex = compound.getInteger(TAG_NEXTINDEX);

        NBTTagList weights = compound.getTagList(TAG_MAPWEIGHTS, Constants.NBT.TAG_FLOAT);
        for (int i = 0; i < BLOCKS_IN_RADIUS; i++) {
            weightForColumn[i] = weights.getFloatAt(i);
        }

        NBTTagList elevations = compound.getTagList(TAG_MAPELEVATIONS, Constants.NBT.TAG_FLOAT);
        for(int i = 0; i < BLOCKS_IN_RADIUS; i++) {
            elevationForColumn[i] = elevations.getFloatAt(i);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(TAG_SIZE, size);
        compound.setInteger(TAG_NEXTINDEX, nextIndex);

        NBTTagList weights = new NBTTagList();
        for (int i = 0; i < BLOCKS_IN_RADIUS; i++) {
            weights.set(i, new NBTTagFloat(weightForColumn[i]));
        }
        compound.setTag(TAG_MAPWEIGHTS, weights);

        NBTTagList elevations = new NBTTagList();
        for (int i = 0; i < BLOCKS_IN_RADIUS; i++) {
            elevations.set(i, new NBTTagFloat(elevationForColumn[i]));
        }
        compound.setTag(TAG_MAPELEVATIONS, elevations);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (world.isRemote)
            return;

        updateBlockCoords();

        checkNextColumn();

        updateMovement();
    }

    private void checkNextColumn() {
        int offsetX = nextIndex % BLOCK_INFLUENCE_DIAMETER - BLOCK_INFLUENCE_RADIUS;
        int offsetZ = nextIndex / BLOCK_INFLUENCE_DIAMETER - BLOCK_INFLUENCE_RADIUS;

        int x = blockX + offsetX;
        int z = blockZ + offsetZ;

        weightForColumn[nextIndex] = 0;
        elevationForColumn[nextIndex] = 0;

        int maxY = world.getHeight(x, z);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(x, 0, z, x, maxY, z)) {
            double wt = LeyWebHelper.getLeyWeightForBlock(world, pos);
            weightForColumn[nextIndex] += wt;
            elevationForColumn[nextIndex] += wt * pos.getY();
        }

        nextIndex += CHECK_INDEX_STEP;
        nextIndex %= BLOCKS_IN_RADIUS;
    }

    private void updateBlockCoords() {
        int newBlockX = MathHelper.floor(posX);
        int newBlockZ = MathHelper.floor(posZ);

        if ( blockX != newBlockX || blockZ != newBlockZ ) {
            Arrays.fill(weightForColumn, 0);
            Arrays.fill(elevationForColumn, 0);

            blockX = newBlockX;
            blockZ = newBlockZ;
        }
    }

    private void updateMovement() {
        Pair<Vec3d, Double> target = getMoveOffsetAndWeight();
        Vec3d offset = target.getLeft();
        double weight = target.getRight();
        Vec3d accel = offset.normalize().scale(weight * BLOCK_ATTRACT_FACTOR / (offset.lengthSquared()));
        // TODO: interaction with neighbors
        addVelocity(accel.x, accel.y, accel.z);
    }

    public Pair<Vec3d, Double> getMoveOffsetAndWeight() {
        Vec3d target = new Vec3d(0,0,0);
        double totalWeight = 0;

        for (int x = 0; x < BLOCK_INFLUENCE_DIAMETER; x++) {
            for (int z = 0; z < BLOCK_INFLUENCE_DIAMETER; z++) {
                int ix = x * BLOCK_INFLUENCE_DIAMETER + z;
                double weight = weightForColumn[ix];
                target = target.addVector(weight * x, elevationForColumn[ix], weight * z);
                totalWeight += weight;
            }
        }

        target = target.scale(1 / totalWeight);

        return new ImmutablePair<>(target, totalWeight);
    }
}
