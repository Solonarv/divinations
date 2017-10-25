package solonarv.mods.divinations.common.locator;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class LocatorResultEntity extends  LocatorResult {

    private final Entity entity;

    protected LocatorResultEntity(Entity entity) {
        super(LocatorResultType.ENTITY);
        this.entity = entity;
    }

    @Override
    public Vec3d getPosition() {
        return this.entity.getPositionVector();
    }

    public Entity getEntity() {
        return this.entity;
    }
}
