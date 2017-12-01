package solonarv.mods.divinations.common.locator.filter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

public abstract class AbstractFilter<T extends ILocatorResult> implements IFilter<T> {

    @Override
    public boolean canActOnClass(Class<? extends T> cls) {
        return getInputClass().isAssignableFrom(cls);
    }

    // Utility method to create filter using lambda notation
    public static <T extends ILocatorResult> AbstractFilter<T> makeFilter(Class<T> cls, ISimpleFilter<T> filter) {
        return new AbstractFilter<T>() {
            @Override
            public boolean matches(World world, Vec3d position, EntityPlayer user, T candidate) {
                return filter.matches(world, position ,user ,candidate);
            }

            @Override
            public Class<T> getInputClass() {
                return cls;
            }
        };
    }

}
