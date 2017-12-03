package solonarv.mods.thegreatweb.common.locator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.lib.ICheckedConsumer;
import solonarv.mods.thegreatweb.common.locator.filter.Filters;
import solonarv.mods.thegreatweb.common.locator.filter.IFilter;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class BaseLocator<T extends ILocatorResult> implements ICheckedConsumer {
    abstract Iterable<T> findResultsRaw(World world, Vec3d position, EntityPlayer user);

    public List<T> findResults(World world, Vec3d position, EntityPlayer user, IFilter<T> filter) {
        return StreamSupport.stream(findResultsRaw(world, position, user).spliterator(), false)
                .filter(obj -> filter.matches(world, position, user, obj))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<T> findResults(World world, Vec3d position, EntityPlayer user, List<IFilter<T>> filters) {
        return findResults(world, position, user, Filters.combineAll(filters, getInputClass()));
    }

    @Override
    public boolean canActOnClass(Class cls) {
        return cls.isAssignableFrom(getInputClass());
    }
}
