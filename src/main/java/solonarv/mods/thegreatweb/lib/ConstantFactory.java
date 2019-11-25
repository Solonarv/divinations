package solonarv.mods.thegreatweb.lib;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class ConstantFactory<T> implements IFactoryNBT<T> {
    private final T obj;
    private final Identifier id;

    @SuppressWarnings("unchecked")
    public ConstantFactory(Identifier id, T obj) {
        this.obj = obj;
        this.id = id;
        // This cast is safe as long as we actually get passed a T
    }

    @Override
    public T readNBT(CompoundTag tag) {
        return obj;
    }

    @Override
    public Identifier getResourceName() {
        return id;
    }
}
