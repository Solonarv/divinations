package solonarv.mods.divinations.common.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public interface IFactoryNBT<T> {
    T readNBT(NBTTagCompound tag);

    ResourceLocation getResourceName();
}
