package solonarv.mods.divinations.common.locator.consumer;

import net.minecraft.util.ResourceLocation;
import solonarv.mods.divinations.common.constants.Misc;

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
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that ID is already in use!", consumer, id));
        if (consumers.containsValue(consumer))
            throw new IllegalStateException(String.format("Attempted to register filter %s as %s, but that class is already registered!", consumer, id));
        consumers.put(id, consumer);
    }

    public static void registerConsumer(String id, IConsumer consumer) {
        String[] split = ResourceLocation.splitObjectName(id);
        if (split[0] == null) {
            split[0] = Misc.MOD_ID;
        }
        registerConsumer(new ResourceLocation(split[0], split[1]), consumer);
    }
}
