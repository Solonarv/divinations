package solonarv.mods.thegreatweb.common.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.leyweb.AbstractLeyWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyWebServer;

@SideOnly(Side.SERVER)
public class LeyWebSync {
    @SubscribeEvent
    public static void sendWebData(ChunkWatchEvent.Watch event) {
        ChunkPos pos = event.getChunk();

        int groupCenter = AbstractLeyWeb.GROUP_SIZE_CHUNKS / 2;

        // only send data if the chunk being watched is the center chunk of that group,
        // to prevent sending each group multiple times.
        if (pos.x % AbstractLeyWeb.GROUP_SIZE_CHUNKS != groupCenter
                || pos.z % AbstractLeyWeb.GROUP_SIZE_CHUNKS != groupCenter)
            return;

        EntityPlayerMP player = event.getPlayer();
        LeyWebServer web = LeyWebServer.get();

        int groupX = Math.floorDiv(pos.x, AbstractLeyWeb.GROUP_SIZE_CHUNKS);
        int groupZ = Math.floorDiv(pos.z, AbstractLeyWeb.GROUP_SIZE_CHUNKS);
        TheGreatWeb.logger.debug(String.format("Sending web data for group <%d, %d>.", groupX, groupZ));

        LeyWebDataPacket packet = new LeyWebDataPacket().setFromData(web, groupX, groupZ);

        PacketHandler.channel.sendTo(packet, player);
    }
}
