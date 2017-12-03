package solonarv.mods.thegreatweb.common.locator.result;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityResult<E extends Entity> implements ILocatorResult {

    private final E entity;

    public EntityResult(E entity) {
        this.entity = entity;
    }


    @Override
    public World getWorld() {
        return entity.world;
    }

    @Override
    public Vec3d getResultLocation() {
        return null;
    }

    public E getEntity() {
        return entity;
    }
}
