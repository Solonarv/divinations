package solonarv.mods.divinations.common.lib;

import net.minecraft.nbt.NBTTagCompound;

public class ConstantFactory<T> implements IFactoryNBT<T> {
    private final T obj;

    public ConstantFactory(T obj) {
        this.obj = obj;
    }

    @Override
    public T readNBT(NBTTagCompound tag) {
        return obj;
    }
}
