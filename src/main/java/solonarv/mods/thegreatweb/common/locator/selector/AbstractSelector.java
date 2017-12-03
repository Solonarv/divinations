package solonarv.mods.thegreatweb.common.locator.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

import java.util.List;

public abstract class AbstractSelector<T extends ILocatorResult> implements ISelector<T> {
    @Override
    public boolean canActOnClass(Class<? extends T> cls) {
        return getInputClass().isAssignableFrom(cls);
    }

    // Utility method to create Selector using lambda notation
    public static <T extends ILocatorResult> AbstractSelector<T> makeSelector(Class<T> cls, ISimpleSelector<T> selector) {
        return new AbstractSelector<T>() {
            @Override
            public List<T> select(World world, Vec3d position, EntityPlayer user, List<T> candidates) {
                return selector.select(world, position ,user ,candidates);
            }

            @Override
            public Class<T> getInputClass() {
                return cls;
            }
        };
    }

}
