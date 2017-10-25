package solonarv.mods.divinations.common.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import solonarv.mods.divinations.common.locator.base.LocatorResult;
import solonarv.mods.divinations.common.selector.base.ISelector;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Selectors {
    public static ISelector ALL = (owner, center, results) -> results;

    public static ISelector RANDOM = (owner, center, results) -> Collections.singletonList(results.get(owner.getRNG().nextInt(results.size())));

    public static ISelector nearestWithin(double radius, int limit) {
        return new ISelector() {
            @Override
            public List<LocatorResult> makeSelection(EntityPlayer owner, Vec3d center, List<LocatorResult> results) {
                return results.stream()
                        .filter(locatorResult -> locatorResult.getPosition().distanceTo(center) <= radius)
                        .limit(limit)
                        .collect(Collectors.toList());
            }
        };
    }

    public static ISelector nearest(int limit) {
        return new ISelector() {
            @Override
            public List<LocatorResult> makeSelection(EntityPlayer owner, Vec3d center, List<LocatorResult> results) {
                return results.stream()
                        .limit(limit)
                        .collect(Collectors.toList());
            }
        };
    }

    @Nonnull
    public static ISelector fromNBT(NBTTagCompound tag) {
        switch (tag.getString("Type")) {
            case "all":
            default:
                return ALL;
            case "random":
                return RANDOM;
            case "nearest":
                int limit = 1;
                if (tag.hasKey("limit", Constants.NBT.TAG_INT)){
                    limit = tag.getInteger("limit");
                }
                if (tag.hasKey("radius", Constants.NBT.TAG_DOUBLE)){
                    return nearestWithin(tag.getDouble("radius"), limit);
                } else {
                    return nearest(limit);
                }
        }
    }
}
