package solonarv.mods.thegreatweb.lib.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class NBTHelper {
    private NBTHelper(){}

    public static Vec3d getVec3d(CompoundTag compound, String keyTemplate) {
        return new Vec3d(
                  compound.getDouble(keyTemplate + "_X")
                , compound.getDouble(keyTemplate + "_Y")
                , compound.getDouble(keyTemplate + "_Z"));
    }

    public static void writeVec3d(CompoundTag compound, String keyTemplate, Vec3d vec3d) {
        compound.putDouble(keyTemplate + "_X", vec3d.x);
        compound.putDouble(keyTemplate + "_Y", vec3d.y);
        compound.putDouble(keyTemplate + "_Z", vec3d.z);
    }

    public static <T> ListTag writeMany(Iterable<T> items, Function<T, Tag> serializer) {
        ListTag nbt = new ListTag();
        for (T item : items){
            nbt.add(serializer.apply(item));
        }
        return nbt;
    }

    public static <T> void deserializeListWith(ListTag tags, Function<CompoundTag, T> read, ArrayList<T> target, Consumer<T> extra) {
        int tagCount = tags.size();
        target.ensureCapacity(tagCount);
        for (int i = 0; i < tagCount; i++) {
            T item = read.apply(tags.getCompoundTag(i));
            target.set(i, item);
            extra.accept(item);
        }
    }
}
