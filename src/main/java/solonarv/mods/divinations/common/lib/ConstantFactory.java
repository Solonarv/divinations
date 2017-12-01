package solonarv.mods.divinations.common.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ConstantFactory<T> implements IFactoryNBT<T> {
    private final T obj;
    private final ResourceLocation id;
    private final Class<T> cls;

    @SuppressWarnings("unchecked")
    public ConstantFactory(ResourceLocation id, T obj) {
        this.obj = obj;
        this.id = id;
        // This cast is safe as long as we actually get passed a T
        this.cls = (Class<T>) obj.getClass();
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
