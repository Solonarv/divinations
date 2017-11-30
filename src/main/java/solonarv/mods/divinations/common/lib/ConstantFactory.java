package solonarv.mods.divinations.common.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ConstantFactory<T> implements IFactoryNBT<T> {
    private final T obj;
    private final ResourceLocation id;

    public ConstantFactory(ResourceLocation id, T obj) {
        this.obj = obj;
        this.id = id;
    }

    @Override
    public T readNBT(NBTTagCompound tag) {
        return obj;
    }

    @Override
    public ResourceLocation getResourceName() {
        return id;
    }
}
