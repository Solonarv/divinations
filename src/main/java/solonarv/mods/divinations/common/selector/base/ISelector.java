package solonarv.mods.divinations.common.selector.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import java.util.List;

public interface ISelector {
    List<LocatorResult> makeSelection(EntityPlayer owner, Vec3d center, List<LocatorResult> results);
}
