package solonarv.mods.divinations.common.locator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import static solonarv.mods.divinations.common.constants.Misc.MOD_ID;

public class Locators {

    private static final Map<ResourceLocation, LocatorFactory> REGISTRY = new HashMap<>();

    public static final LocatorFactory blockLocatorFactory = new BlockLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "block"));
    public static final LocatorFactory entityLocatorFactory = new EntityLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "entity"));
    public static final LocatorFactory lightLevelLocatorFactory = new BlockLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "lightLevel"));

    @Nullable
    public static ILocator fromNBT(NBTTagCompound tag) {
        if (!tag.hasKey("type", 8) || !tag.hasKey("data", 10))
            return null;
        LocatorFactory factory = REGISTRY.get(new ResourceLocation(tag.getString("type")));
        if (factory == null)
            return null;
        return factory.fromNBT(tag.getCompoundTag("data"));
    }

    public static void registerDefaultLocators(){
        REGISTRY.put(blockLocatorFactory.getRegistryName(), blockLocatorFactory);
        REGISTRY.put(entityLocatorFactory.getRegistryName(), entityLocatorFactory);
        REGISTRY.put(lightLevelLocatorFactory.getRegistryName(), lightLevelLocatorFactory);
    }
}
