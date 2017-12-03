package solonarv.mods.thegreatweb.common.locator.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.lib.ICheckedConsumer;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

import java.util.List;

public interface ISelector<T extends ILocatorResult> extends ICheckedConsumer<T>, ISimpleSelector<T> {
}
