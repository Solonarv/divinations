package solonarv.mods.divinations.common.locator.filter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.constants.Misc;
import solonarv.mods.divinations.common.lib.ConstantFactory;
import solonarv.mods.divinations.common.lib.IFactoryNBT;
import solonarv.mods.divinations.common.locator.filter.block.LightLevelFilter;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Filters {
    private static Map<ResourceLocation, IFactoryNBT<? extends IFilter>> filters = new HashMap<>();
    private static boolean initDone = false;

    public static final IFilter<ILocatorResult> ALL = (world, pos, user, cand) -> true;

    public static void init(){
        if (initDone)
            return;
        registerFilter("all", new ConstantFactory<IFilter>(ALL));
        registerFilter("lightLevel", LightLevelFilter.factory);
        initDone = true;
    }

    private static void registerFilter(String id, IFactoryNBT<? extends IFilter> factory) {
        String[] split = ResourceLocation.splitObjectName(id);
        if (split[0] == null) {
            split[0] = Misc.MOD_ID;
        }
        registerFilter(new ResourceLocation(split[0], split[1]), factory);
    }

    public static void registerFilter(ResourceLocation id, IFactoryNBT<? extends IFilter> factory) {
        if (filters.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that ID is already in use!", factory, id));
        if (filters.containsValue(factory))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that class is already registered!", factory, id));
        filters.put(id, factory);
    }

    public static <T extends ILocatorResult> IFilter<T> and(IFilter<? super T> filter0, IFilter<? super T> filter1) {
        return (world, pos, user, candidate) -> filter0.matches(world, pos, user, candidate) && filter1.matches(world, pos, user, candidate);
    }

    public static List<IFilter> fromNBT(NBTTagList filterNBT) {
        List<IFilter> result = new LinkedList<>();
        for (NBTBase filterTag : filterNBT) {
            if (!(filterTag instanceof NBTTagCompound))
                continue;
            NBTTagCompound compound = (NBTTagCompound) filterTag;
            ResourceLocation filterID = new ResourceLocation(compound.getString("filterType"));

            IFactoryNBT<? extends IFilter> factory = getFilterByID(filterID);
            IFilter filter = factory.readNBT(compound);
            result.add(filter);
        }
        return result;
    }

    private static IFactoryNBT<? extends IFilter> getFilterByID(ResourceLocation filterID) {
        return filters.get(filterID);
    }
}
