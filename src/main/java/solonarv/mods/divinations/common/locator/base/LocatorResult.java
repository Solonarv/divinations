package solonarv.mods.divinations.common.locator.base;

import net.minecraft.util.math.Vec3d;

public abstract class  LocatorResult {

    abstract public Vec3d getPosition();

    public static class BlockPos extends LocatorResult {


        private final net.minecraft.util.math.BlockPos position;

        public BlockPos(net.minecraft.util.math.BlockPos position) {
            this.position = position;
        }

        @Override
        public Vec3d getPosition() {
            return new Vec3d(position.getX(), position.getY(), position.getZ());
        }

        public net.minecraft.util.math.BlockPos getBlockPos() {
            return this.position;
        }
    }

    public static class Entity extends  LocatorResult {

        // Not the most elegant, but name clashes are name clashes.
        // Having a nice name for this class is worth an awkward field declaration.
        private final net.minecraft.entity.Entity entity;

        protected Entity(net.minecraft.entity.Entity entity) {
            this.entity = entity;
        }

        @Override
        public Vec3d getPosition() {
            return this.entity.getPositionVector();
        }

        public net.minecraft.entity.Entity getEntity() {
            return this.entity;
        }

        @Override
        public String toString() {
            return entity.toString();
        }
    }
}
