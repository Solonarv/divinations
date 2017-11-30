package solonarv.mods.divinations.common.locator.consumer;

import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.constants.Misc;
import solonarv.mods.divinations.common.lib.Util;

import java.util.HashMap;
import java.util.Map;

public class Consumers {
    private static Map<ResourceLocation, IConsumer> consumers = new HashMap<>();
    private static boolean initDone = false;

    public static void init(){
        if (initDone)
            return;
        registerConsumer("chat", ChatConsumer.instance);
    }

    public static void registerConsumer(ResourceLocation id, IConsumer consumer) {
        if (consumers.containsKey(id))
            throw new IllegalStateException(String.format("Attempted to register consumer %s as %s, but that ID is already in use!", consumer, id));
        if (consumers.containsValue(consumer))
            throw new IllegalStateException(String.format("Attempted to register consumer %s as %s, but that class is already registered!", consumer, id));
        consumers.put(id, consumer);
    }

    public static void registerConsumer(String id, IConsumer consumer) {
        registerConsumer(Util.resourceLocationWithDefaultDomain(id), consumer);
    }

    public static IConsumer getConsumerByID(ResourceLocation id){
        return consumers.get(id);
    }

    public static IConsumer fromString(String id) {
        return getConsumerByID(Util.resourceLocationWithDefaultDomain(id));
    }
}
