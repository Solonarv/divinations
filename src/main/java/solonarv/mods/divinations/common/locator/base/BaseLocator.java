package solonarv.mods.divinations.common.locator.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseLocator implements IForgeRegistryEntry<BaseLocator> {

    private ResourceLocation registryName;

    public abstract IConcreteLocator fromNBT(@Nonnull NBTTagCompound tag);

    @Override
    public BaseLocator setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    @Override
    public Class<BaseLocator> getRegistryType() {
        return BaseLocator.class;
    }
}
