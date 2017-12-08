package solonarv.mods.thegreatweb.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import solonarv.mods.thegreatweb.common.leyweb.AbstractLeyWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebServer;

public class RequestLeyWebDataPacket extends LeyWebPacketBase {

    public static class Handler implements IMessageHandler<RequestLeyWebDataPacket, LeyWebDataPacket> {

        @Override
        public LeyWebDataPacket onMessage(RequestLeyWebDataPacket message, MessageContext ctx) {
            World world = ctx.getServerHandler().player.world;
            BlockPos minCorner = new BlockPos(message.groupX * AbstractLeyWeb.GROUP_SIZE_BLOCKS, 0, message.groupZ * AbstractLeyWeb.GROUP_SIZE_BLOCKS);
            boolean loaded = false;
            for (int dx = 0; dx < 3; dx++)
                for (int dz = 0; dz < 3; dz++)
                    if (world.isBlockLoaded(minCorner.add(dx << 4, 0, dz <<4))) {
                        loaded = true;
                        break;
                    }
            if (!loaded)
                return null;
            return new LeyWebDataPacket().setFromData(LeyWebServer.get(), message.groupX, message.groupZ);
        }
    }
}
