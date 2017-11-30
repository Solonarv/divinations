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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Filters {
    private static Map<ResourceLocation, IFactoryNBT<? extends IFilter>> filters = new HashMap<>();
    private static boolean initDone = false;

    public static final IFilter ALL = (world, pos, user, cand) -> true;

    public static void init(){
        if (initDone)
            return;
        registerFilter(new ConstantFactory<>(Util.withModID("all"), ALL));
        registerFilter(LightLevelFilter.factory);
        initDone = true;
    }

    public static void registerFilter(IFactoryNBT<? extends IFilter> factory) {
        ResourceLocation id = factory.getResourceName();
        if (filters.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that ID is already in use!", factory, id));
        if (filters.containsValue(factory))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that class is already registered!", factory, id));
        filters.put(id, factory);
    }

    public static <T extends ILocatorResult> IFilter<T> and(IFilter<? super T> filter0, IFilter<? super T> filter1) {
        return (world, pos, user, candidate) -> filter0.matches(world, pos, user, candidate) && filter1.matches(world, pos, user, candidate);
    }

    public static <T extends ILocatorResult> IFilter<T> combineAll(List<IFilter<? super T>> filters) {
        if (filters.isEmpty()) {
            return ALL;
        } else {
            return (world, position, user, candidate) -> {
                for (IFilter filter : filters) {
                    if (!filter.matches(world, position, user, candidate))
                        return false;
                }
                return true;
            };
        }
    }

    public static List<IFilter<? super ILocatorResult>> fromNBT(NBTTagList filterNBT) {
        List<IFilter<? super ILocatorResult>> result = new LinkedList<>();
        for (NBTBase filterTag : filterNBT) {
            if (!(filterTag instanceof NBTTagCompound))
                continue;
            NBTTagCompound compound = (NBTTagCompound) filterTag;
            ResourceLocation filterID = Util.resourceLocationWithDefaultDomain(compound.getString("filterType"));

            IFactoryNBT<? extends IFilter> factory = getFilterByID(filterID);
            if (factory == null)
                continue;
            IFilter filter = factory.readNBT(compound);
            result.add(filter);
        }
        return result;
    }

    private static IFactoryNBT<? extends IFilter> getFilterByID(ResourceLocation filterID) {
        System.out.println("Retrieving filter with ID " + filterID);
        return filters.get(filterID);
    }
}
