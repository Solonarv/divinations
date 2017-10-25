package solonarv.mods.divinations.common.resultdisplay;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import java.util.List;

public interface IResultDisplay {
    void displayResults(EntityPlayer owner, Vec3d center, LocatorResult result);
}
