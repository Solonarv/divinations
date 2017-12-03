package solonarv.mods.thegreatweb.common.locator.selector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.lib.Util;
import solonarv.mods.thegreatweb.common.locator.result.BlockResult;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Selectors {
    private static Map<ResourceLocation, ISelector<ILocatorResult>> selectors = new HashMap<>();
    private static boolean initDone = false;

    public static final ISelector NONE = AbstractSelector.makeSelector(ILocatorResult.class,(world, position, user, candidates) -> Collections.emptyList());
    public static final ISelector ALL = AbstractSelector.makeSelector(ILocatorResult.class, (world, position, user, candidates) -> candidates);
    public static final ISelector<? extends  ILocatorResult> RANDOM = AbstractSelector.makeSelector(ILocatorResult.class, new SingleSelector<ILocatorResult>() {
        @Override
        public ILocatorResult selectSingle(World world, Vec3d position, EntityPlayer user, List<ILocatorResult> candidates) {
            int index = user.getRNG().nextInt(candidates.size());
            return candidates.get(index);
        }
    });
    public static final ISelector<? extends ILocatorResult> NEAREST = AbstractSelector.makeSelector(ILocatorResult.class, new SingleSelector<ILocatorResult>() {
        @Override
        public ILocatorResult selectSingle(World world, Vec3d position, EntityPlayer user, List<ILocatorResult> candidates) {
            double nearestDist = Double.POSITIVE_INFINITY;
            ILocatorResult nearest = null;
            for (ILocatorResult cand : candidates) {
                double dist = position.squareDistanceTo(cand.getResultLocation());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearest = cand;
                }
            }
            return nearest;
        }
    });

    public static final ISelector<? extends ILocatorResult> FURTHEST = AbstractSelector.makeSelector(ILocatorResult.class, new SingleSelector<ILocatorResult>() {
        @Override
        public ILocatorResult selectSingle(World world, Vec3d position, EntityPlayer user, List<ILocatorResult> candidates) {
            double nearestDist = Double.NEGATIVE_INFINITY;
            ILocatorResult furthest = null;
            for (ILocatorResult cand : candidates) {
                double dist = position.squareDistanceTo(cand.getResultLocation());
                if (dist > nearestDist) {
                    nearestDist = dist;
                    furthest = cand;
                }
            }
            return furthest;
        }
    });

    public static void init() {
        if (initDone)
            return;
        registerSelector(Util.withModID("none"), NONE);
        registerSelector(Util.withModID("all"), ALL);
        registerSelector(Util.withModID("random"), RANDOM);
        registerSelector(Util.withModID("nearest"), NEAREST);
        registerSelector(Util.withModID("furthest"), FURTHEST);
        initDone = true;
    }

    // The unchecked cast is a) safe (since we're just dumping the argument into a map), and b) needed
    // because the map is sort-of heterogenous.
    @SuppressWarnings("unchecked")
    public static void registerSelector(ResourceLocation id, ISelector<? extends  ILocatorResult> selector) {
        if (selectors.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register selector %s as %s, but that ID is already in use!", selector, id));
        //noinspection SuspiciousMethodCalls
        if (selectors.containsValue(selector))
            throw new IllegalStateException(String.format("Attempted to register selector %s as %s, but that selector is already registered!", selector, id));
        selectors.put(id, (ISelector<ILocatorResult>) selector);
    }

    public static <T extends ILocatorResult> ISelector<T> fromString(String str, Class<T> cls) {
        System.out.println("Retrieving selector from string " + str);
        return getSelectorFromID(Util.resourceLocationWithDefaultDomain(str), cls);
    }

    // See comments on Filters::fromNBT
    @SuppressWarnings("unchecked")
    public static <T extends ILocatorResult> ISelector<T> getSelectorFromID(ResourceLocation id, Class<T> cls) {
        System.out.println("Retrieving selector with ID " + id);
        ISelector<ILocatorResult> selector = selectors.get(id);
        if (selector != null && selector.canActOnClass(cls))
            return (ISelector<T>) selector;
        return null;
    }
}
