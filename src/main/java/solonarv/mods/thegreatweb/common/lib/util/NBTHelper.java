package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import solonarv.mods.thegreatweb.common.lightweb.WebFace;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class NBTHelper {
    @Nonnull
    public static Vec3d getVec3d(NBTTagCompound compound, String keyTemplate) {
        return new Vec3d(
                  compound.getDouble(keyTemplate + "_X")
                , compound.getDouble(keyTemplate + "_Y")
                , compound.getDouble(keyTemplate + "_Z"));
    }

    public static void writeVec3d(NBTTagCompound compound, String keyTemplate, Vec3d vec3d) {
        compound.setDouble(keyTemplate + "_X", vec3d.x);
        compound.setDouble(keyTemplate + "_Y", vec3d.y);
        compound.setDouble(keyTemplate + "_Z", vec3d.z);
    }

    @Nonnull
    public static <T> NBTTagList writeMany(Iterable<T> items, Function<T, NBTBase> serializer) {
        NBTTagList nbt = new NBTTagList();
        for (T item : items){
            nbt.appendTag(serializer.apply(item));
        }
        return nbt;
    }

    public static <T> void deserializeListWith(NBTTagList tags, Function<NBTTagCompound, T> read, ArrayList<T> target, Consumer<T> extra) {
        int tagCount = tags.tagCount();
        target.ensureCapacity(tagCount);
        for (int i = 0; i < tagCount; i++) {
            T item = read.apply(tags.getCompoundTagAt(i));
            target.set(i, item);
            extra.accept(item);
        }
    }
}
