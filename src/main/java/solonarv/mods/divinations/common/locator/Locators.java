package solonarv.mods.divinations.common.locator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.base.BaseLocator;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static solonarv.mods.divinations.common.constants.Misc.MOD_ID;

public class Locators {

    private static final Map<ResourceLocation, BaseLocator> REGISTRY = new HashMap<>();

    public static final BaseLocator blockLocatorFactory = new BlockLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "block"));
    public static final BaseLocator entityLocatorFactory = new EntityLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "entity"));
    public static final BaseLocator lightLevelLocatorFactory = new BlockLocatorFactory().setRegistryName(new ResourceLocation(MOD_ID, "lightLevel"));
    public static final IConcreteLocator EMPTY = new IConcreteLocator() {
        @Nonnull
        @Override
        public List<LocatorResult> getResults(World world, EntityPlayer owner, Vec3d position) {
            return Collections.emptyList();
        }
    };

    @Nullable
    public static IConcreteLocator fromNBT(NBTTagCompound tag) {
        if (!tag.hasKey("type", 8) || !tag.hasKey("data", 10))
            return EMPTY;
        BaseLocator factory = REGISTRY.get(new ResourceLocation(tag.getString("type")));
        if (factory == null)
            return EMPTY;
        return factory.fromNBT(tag.getCompoundTag("data"));
    }

    public static void registerDefaultLocators(){
        REGISTRY.put(blockLocatorFactory.getRegistryName(), blockLocatorFactory);
        REGISTRY.put(entityLocatorFactory.getRegistryName(), entityLocatorFactory);
        REGISTRY.put(lightLevelLocatorFactory.getRegistryName(), lightLevelLocatorFactory);
    }
}
