package solonarv.mods.thegreatweb.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebHelper;
import solonarv.mods.thegreatweb.common.lib.util.EntityHelper;
import solonarv.mods.thegreatweb.common.lib.util.MathUtil;
import solonarv.mods.thegreatweb.common.lib.util.NBTHelper;

import javax.annotation.Nonnull;

public class EntityLeyNode extends Entity {

    private static final String TAG_SIZE = "size";
    private double size = 0;
    private boolean hasMoved = false;

    private int blockX, blockY, blockZ;

    private static final String TAG_ACCELERATION = "acceleration";
    private Vec3d acceleration = Vec3d.ZERO;

    private static int BLOCK_INFLUENCE_RADIUS = 48;
    private static int BLOCK_INFLUENCE_DIAMETER = 2 * BLOCK_INFLUENCE_RADIUS + 1;
    public static int BLOCKS_IN_RADIUS = BLOCK_INFLUENCE_DIAMETER * BLOCK_INFLUENCE_DIAMETER;

    private static final double BLOCK_ATTRACT_FACTOR = 1e-6;

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

        updateMovement();

    }

    private void updateBlockCoords() {
        int newX = (int) posX;
        int newY = (int) posY;
        int newZ = (int) posZ;

        if (blockX != newX || blockY != newY || blockZ != newZ) {
            blockX = newX;
            blockY = newY;
            blockZ = newZ;

            hasMoved = true;
        } else {
            hasMoved = false;
        }

    }

    private void updateMovement() {
        // NOTE: only scans surroundings once every 100 ticks or after changing block coords, to combat lag.
        if (this.ticksExisted % 100 == 0 || hasMoved) {
            acceleration = getNetForce().scale(1 / (1+size));
            hasMoved = false;
        }
        addVelocity(acceleration.x, acceleration.y, acceleration.z);
        markVelocityChanged();
        move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Nonnull
    private Vec3d getNetForce() {
        // TODO interaction with neighbors
        return getForceFromBlocks();
    }

    @Nonnull
    private Vec3d getForceFromBlocks() {
        Vec3d force = new Vec3d(0,0,0);

        // TODO check world height so we don't scan a whole bunch of air?
        for (BlockPos.MutableBlockPos pos :
                BlockPos.getAllInBoxMutable(
                blockX - BLOCK_INFLUENCE_RADIUS, 0, blockZ - BLOCK_INFLUENCE_RADIUS,
                blockX + BLOCK_INFLUENCE_RADIUS, 255, blockZ + BLOCK_INFLUENCE_RADIUS
                )
            ) {
            double blockWeight = LeyWebHelper.getLeyWeightForBlock(world, pos);

            double relX = posX - pos.getX();
            double relY = posY - pos.getY();
            double relZ = posZ - pos.getZ();

            double distSq = MathUtil.normSq(relX, relY, relZ);
            if (distSq == 0) continue;
            double scale = blockWeight / Math.sqrt(distSq) / distSq;

            force = force.addVector(relX * scale, relY * scale, relZ * scale);
        }

        force = force.scale(-BLOCK_ATTRACT_FACTOR);

        return force;
    }
}
