package solonarv.mods.thegreatweb.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebHelper;
import solonarv.mods.thegreatweb.common.lib.util.MathUtil;
import solonarv.mods.thegreatweb.common.lib.util.NBTHelper;

import javax.annotation.Nonnull;

public class EntityLeyNode extends Entity {

    private static final String TAG_SIZE = "size";
    private double size = 0;

    private int blockX, blockY, blockZ;

    private static final String TAG_ACCELERATION = "acceleration";
    private Vec3d acceleration = Vec3d.ZERO;

    private int nextIndex = 0;
    // Cached forces from all blocks in a column. These are NOT persisted because they are too large, and are
    // totally refreshed/rebuilt every ~100 ticks anyway.
    private float[] totalForceColX = new float[COLUMN_BATCH_COUNT_TOTAL];
    private float[] totalForceColY = new float[COLUMN_BATCH_COUNT_TOTAL];
    private float[] totalForceColZ = new float[COLUMN_BATCH_COUNT_TOTAL];

    // Math (tm)
    private static int RADIUS_FACTOR = 10;

    /** process columns in batches of 3x3 */
    private static int COLUMN_BATCH_SIZE = 3;
    private static int COLUMN_BATCH_COUNT_1D = 2 * RADIUS_FACTOR + 1;
    private static int COLUMN_BATCH_COUNT_TOTAL = COLUMN_BATCH_COUNT_1D * COLUMN_BATCH_COUNT_1D;
    private static int COLUMN_BATCH_STEP = MathUtil.nextLowestPowerOf2(COLUMN_BATCH_COUNT_TOTAL);

    private static int BLOCK_INFLUENCE_RADIUS = 3 * RADIUS_FACTOR + 1;
    private static int BLOCK_INFLUENCE_DIAMETER = 2 * BLOCK_INFLUENCE_RADIUS + 1;
    public static int BLOCKS_IN_RADIUS = BLOCK_INFLUENCE_DIAMETER * BLOCK_INFLUENCE_DIAMETER;

    private static final double BLOCK_ATTRACT_FACTOR = 1e-7;

    public EntityLeyNode(World worldIn) {
        super(worldIn);
        isImmuneToFire = true;
        noClip = true;
        setGlowing(true);
    }

    @Override
    protected void entityInit() {
        setSize(1, 1);
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        size = compound.getDouble(TAG_SIZE);
        acceleration = NBTHelper.getVec3d(compound, TAG_ACCELERATION);
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setDouble(TAG_SIZE, size);
        NBTHelper.writeVec3d(compound, TAG_ACCELERATION, acceleration);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (world.isRemote)
            return;

        updateBlockCoords();

        scanSomeBlocks();

        updateMovement();

    }

    private void scanSomeBlocks() {
        for (int i = 0; i < 10; i++) {
            scanNextColumnBatch();
        }
    }

    /**
     * Scan the next batch (3x3) of columns.
     */
    private void scanNextColumnBatch() {

        // Coordinate transforms
        int batchX = nextIndex / COLUMN_BATCH_COUNT_1D;
        int batchZ = nextIndex % COLUMN_BATCH_COUNT_1D;

        int realX = 3 * batchX + blockX;
        int realZ = 3 * batchZ + blockZ;

        int minY = Math.max(0, blockY - BLOCK_INFLUENCE_RADIUS);
        int maxY = Math.min(255, blockZ + BLOCK_INFLUENCE_RADIUS);

        // Note: math is done using doubles and only converted to floats at the end
        double forceX = 0, forceY = 0, forceZ = 0;

        for (BlockPos pos : BlockPos.getAllInBoxMutable(realX - 1, minY, realZ - 1, realX + 1, maxY, realZ + 1)) {
            double weight = LeyWebHelper.getLeyWeightForBlock(world, pos);

            double dX = pos.getX() - posX;
            double dY = pos.getY() - posY;
            double dZ = pos.getZ() - posZ;

            double distSq = MathUtil.normSq(dX, dY, dZ);
            double scale = weight / distSq / Math.sqrt(distSq);

            forceX += dX * scale;
            forceY += dY * scale;
            forceZ += dZ * scale;
        }

        // Write values
        totalForceColX[nextIndex] = (float) forceX;
        totalForceColY[nextIndex] = (float) forceY;
        totalForceColZ[nextIndex] = (float) forceZ;

        // Advance index
        nextIndex += COLUMN_BATCH_STEP;
        nextIndex %= COLUMN_BATCH_COUNT_TOTAL;
    }

    /**
     * Update this node's block-space position, and reset the block scan cache if the position changed.
     */
    private void updateBlockCoords() {
        int newX = (int) posX;
        int newY = (int) posY;
        int newZ = (int) posZ;

        if (blockX != newX || blockY != newY || blockZ != newZ) {
            blockX = newX;
            blockY = newY;
            blockZ = newZ;

            // look ma, no allocation!
            Arrays.fill(totalForceColX, 0);
            Arrays.fill(totalForceColY, 0);
            Arrays.fill(totalForceColZ, 0);
        }

    }

    private void updateMovement() {
        acceleration = getNetForce().scale(1 / (1+size));
        addVelocity(acceleration.x, acceleration.y, acceleration.z);
        applyDrag();
        markVelocityChanged();
        move(MoverType.SELF, motionX, motionY, motionZ);
    }

    private void applyDrag() {
        motionY *= 0.9;

        motionX *= 0.97;
        motionZ *= 0.97;
    }

    @Nonnull
    private Vec3d getNetForce() {
        // TODO interaction with neighbors
        return getForceFromBlocks();
    }

    /**
     * Add up the cached partial forces from surrounding blocks.
     * @return a vector describing the total force acting on this node from nearby blocks
     */
    @Nonnull
    private Vec3d getForceFromBlocks() {
        Vec3d force = new Vec3d(0,0,0);

        for (int i = 0; i < COLUMN_BATCH_COUNT_TOTAL; i++) {
            force = force.addVector(totalForceColX[i], totalForceColY[i], totalForceColZ[i]);
        }

        force = force.scale(-BLOCK_ATTRACT_FACTOR);

        return force;
    }
}
