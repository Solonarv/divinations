package solonarv.mods.thegreatweb.lib;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public interface IFactoryNBT<T> {
    T readNBT(CompoundTag tag);

    Identifier getResourceName();
}
