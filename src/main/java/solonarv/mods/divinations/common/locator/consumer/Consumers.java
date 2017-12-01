package solonarv.mods.divinations.common.locator.consumer;

import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.lib.Util;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

import java.util.HashMap;
import java.util.Map;

public class Consumers {
    private static Map<ResourceLocation, IConsumer<ILocatorResult>> consumers = new HashMap<>();
    private static boolean initDone = false;

    public static void init(){
        if (initDone)
            return;
        registerConsumer("chat", ChatConsumer.instance);
        initDone = true;
    }

    @SuppressWarnings("unchecked")
    public static void registerConsumer(ResourceLocation id, IConsumer<? extends ILocatorResult> consumer) {
        if (consumers.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register consumer %s as %s, but that ID is already in use!", consumer, id));
        //noinspection SuspiciousMethodCalls
        if (consumers.containsValue(consumer))
            throw new IllegalStateException(String.format("Attempted to register consumer %s as %s, but that class is already registered!", consumer, id));
        consumers.put(id, (IConsumer<ILocatorResult>) consumer);
    }

    public static void registerConsumer(String id, IConsumer<? extends ILocatorResult> consumer) {
        registerConsumer(Util.resourceLocationWithDefaultDomain(id), consumer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ILocatorResult> IConsumer<T> getConsumerByID(ResourceLocation id, Class<T> cls){
        IConsumer<ILocatorResult> consumer = consumers.get(id);
        if (consumer != null && consumer.canActOnClass(cls))
            return (IConsumer<T>) consumer;
        return null;
    }

    public static <T extends ILocatorResult> IConsumer<T> fromString(String id, Class<T> cls) {
        return getConsumerByID(Util.resourceLocationWithDefaultDomain(id), cls);
    }
}
