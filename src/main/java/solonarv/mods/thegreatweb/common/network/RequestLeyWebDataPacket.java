package solonarv.mods.thegreatweb.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebServer;

public class RequestLeyWebDataPacket extends LeyWebPacketBase {

    public static class Handler implements IMessageHandler<RequestLeyWebDataPacket, LeyWebDataPacket> {

        @Override
        public LeyWebDataPacket onMessage(RequestLeyWebDataPacket message, MessageContext ctx) {
            return new LeyWebDataPacket().setFromData(LeyWebServer.get(), message.groupX, message.groupZ);
        }
    }
}
