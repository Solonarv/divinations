package solonarv.mods.divinations.common.locator.filter;


import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.lib.ConstantFactory;
import solonarv.mods.divinations.common.lib.IFactoryNBT;
import solonarv.mods.divinations.common.lib.Util;
import solonarv.mods.divinations.common.locator.filter.block.LightLevelFilter;
import solonarv.mods.divinations.common.locator.result.BlockResult;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Filters {
    private static Map<ResourceLocation, IFactoryNBT<IFilter<ILocatorResult>>> filters = new HashMap<>();
    private static boolean initDone = false;

    public static final IFilter<ILocatorResult> ALL = AbstractFilter.makeFilter(ILocatorResult.class, (world, pos, user, candidate) -> true);

    @SuppressWarnings("unchecked")
    public static void init(){
        if (initDone)
            return;
        registerFilter(new ConstantFactory<>(Util.withModID("all"), ALL));
        registerFilter(LightLevelFilter.factory);
        initDone = true;
    }

    @SuppressWarnings("unchecked")
    public static void registerFilter(IFactoryNBT<? extends IFilter<? extends ILocatorResult>> factory) {
        ResourceLocation id = factory.getResourceName();
        if (filters.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that ID is already in use!", factory, id));
        if (filters.containsValue(factory))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that class is already registered!", factory, id));
        // This cast is safe: a factory that produces <? extends IFilter<ILocatorResults>> can be used as a factory
        // that produces <IFilter<ILocatorResult>>. Unfortunately, javac is too dumb to figure that out, so we get
        // a warning that we have to suppress.
        filters.put(id, (IFactoryNBT<IFilter<ILocatorResult>>) factory);
    }

    // This is for the "unchecked" cast (see comment below)
    @SuppressWarnings("unchecked")
    public static <T extends ILocatorResult> List<IFilter<T>> fromNBT(NBTTagList filterNBT, Class<T> cls) {
        List<IFilter<T>> result = new LinkedList<>();
        for (NBTBase filterTag : filterNBT) {
            if (!(filterTag instanceof NBTTagCompound))
                continue;
            NBTTagCompound compound = (NBTTagCompound) filterTag;
            ResourceLocation filterID = Util.resourceLocationWithDefaultDomain(compound.getString("filterType"));

            IFactoryNBT<IFilter<ILocatorResult>> factory = getFilterFactoryByID(filterID);

            if (factory == null)
                continue;

            IFilter<ILocatorResult> filter = factory.readNBT(compound);
            if (filter.canActOnClass(cls))
                // This cast is safe because its validity is checked by the above conditional.
                result.add((IFilter<T>) filter);
        }
        return result;
    }

    @Nullable
    private static IFactoryNBT<IFilter<ILocatorResult>> getFilterFactoryByID(ResourceLocation filterID) {
        System.out.println("Retrieving filter with ID " + filterID);
        return filters.get(filterID);
    }

    public static <T extends ILocatorResult> IFilter<T> combineAll(List<IFilter<T>> filters, Class<T> cls) {
        return AbstractFilter.makeFilter(cls, (world, position, user, candidate) -> {
            for (IFilter<T> filter : filters) {
                if (!filter.matches(world, position, user, candidate))
                    return false;
            }
            return true;
        });
    }
}
