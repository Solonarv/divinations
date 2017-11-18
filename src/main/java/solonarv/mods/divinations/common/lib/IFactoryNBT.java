package solonarv.mods.divinations.common.lib;

import net.minecraft.nbt.NBTTagCompound;

public interface IFactoryNBT<T> {
    T readNBT(NBTTagCompound tag);
}
