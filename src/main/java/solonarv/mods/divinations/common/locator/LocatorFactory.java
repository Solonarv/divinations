package solonarv.mods.divinations.common.locator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public abstract class LocatorFactory implements IForgeRegistryEntry<LocatorFactory> {

    private ResourceLocation registryName;

    public abstract ILocator fromNBT(NBTTagCompound tag);

    @Override
    public LocatorFactory setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    @Override
    public Class<LocatorFactory> getRegistryType() {
        return LocatorFactory.class;
    }
}
