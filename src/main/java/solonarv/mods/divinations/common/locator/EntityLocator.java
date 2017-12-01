package solonarv.mods.divinations.common.locator;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import solonarv.mods.divinations.common.locator.result.EntityResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EntityLocator<E extends Entity> extends BaseLocator<EntityResult<E>> {
    public static final double MAX_RADIUS = 32;
    private static final Vec3d AABB_OFFSET = new Vec3d(MAX_RADIUS, MAX_RADIUS, MAX_RADIUS);

    private static final Map<ResourceLocation, EntityLocator> instances = new HashMap<>();

    private final Class<E> entityType;

    private EntityLocator(Class<E> cls) {
        this.entityType = cls;
    }

    @SuppressWarnings("unchecked")
    public static EntityLocator getInstanceFor(ResourceLocation entityType) {
        if (!instances.containsKey(entityType)) {
            EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(entityType);

            if (entityEntry == null)
                throw new IllegalArgumentException("Attempted to get a locator instance for entity %s, but no such entity is registered!");

            instances.put(entityType, new EntityLocator(entityEntry.getEntityClass()));
        }

        return instances.get(entityType);
    }

    @Override
    public List<EntityResult<E>> findResultsRaw(World world, Vec3d position, @Nullable EntityPlayer user) {
        AxisAlignedBB aabb = new AxisAlignedBB(position.subtract(AABB_OFFSET), position.add(AABB_OFFSET));
        return world.getEntitiesWithinAABB(entityType, aabb).stream().map(EntityResult::new).collect(Collectors.toList());
    }

    @Override
    public Class getInputClass() {
        return EntityResult.class;
    }
}
