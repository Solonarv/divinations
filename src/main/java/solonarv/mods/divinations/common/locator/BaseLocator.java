package solonarv.mods.divinations.common.locator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.filter.Filters;
import solonarv.mods.divinations.common.locator.filter.IFilter;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class BaseLocator<T extends ILocatorResult> {
    abstract Iterable<T> findResultsRaw(World world, Vec3d position, EntityPlayer user);

    public List<T> findResults(World world, Vec3d position, EntityPlayer user, IFilter<? super T> filter) {
        return StreamSupport.stream(findResultsRaw(world, position, user).spliterator(), false)
                .filter(obj -> filter.matches(world, position, user, obj))
                .collect(Collectors.toList());
    }

    public List<T> findResults(World world, Vec3d position, EntityPlayer user, List<IFilter<? super T>> filters) {
        return findResults(world, position, user, filters.stream().reduce(Filters.ALL, Filters::and));
    }
}
