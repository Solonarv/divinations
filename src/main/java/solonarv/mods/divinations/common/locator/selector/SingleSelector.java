package solonarv.mods.divinations.common.locator.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.Collections;
import java.util.List;

public abstract class SingleSelector<T extends ILocatorResult> implements ISelector<T> {

    @Override
    public final List<T> select(World world, Vec3d position, EntityPlayer user, List<T> candidates) {
        return Collections.singletonList(selectSingle());
    }

    public abstract T selectSingle();
}
