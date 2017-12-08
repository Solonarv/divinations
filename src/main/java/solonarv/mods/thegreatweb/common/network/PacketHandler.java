package solonarv.mods.thegreatweb.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import solonarv.mods.thegreatweb.common.TheGreatWeb;

public class PacketHandler {
    private static int packetID = 0;

    public static SimpleNetworkWrapper channel = null;

    public static int nextID() {
        return packetID++;
    }

    public static void register(String channelName) {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerPackets();
    }

    public static void registerPackets() {
        TheGreatWeb.logger.info("registering packets!");
        channel.registerMessage(RequestLeyWebDataPacket.Handler.class, RequestLeyWebDataPacket.class, nextID(), Side.SERVER);
        channel.registerMessage(LeyWebDataPacket.Handler.class, LeyWebDataPacket.class, nextID(), Side.CLIENT);
    }


}
