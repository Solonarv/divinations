package solonarv.mods.thegreatweb.client.leyweb;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import solonarv.mods.thegreatweb.common.leyweb.AbstractLeyWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyLine;
import solonarv.mods.thegreatweb.common.leyweb.LeyNode;
import solonarv.mods.thegreatweb.common.leyweb.LeyNodeGroup;
import solonarv.mods.thegreatweb.common.network.LeyWebDataPacket;
import solonarv.mods.thegreatweb.common.network.LeyWebPacketBase;
import solonarv.mods.thegreatweb.common.network.PacketHandler;
import solonarv.mods.thegreatweb.common.network.RequestLeyWebDataPacket;

import java.util.*;

@SideOnly(Side.CLIENT)
public class LeyWebClient extends AbstractLeyWeb {


    // 10 second
    private static final long MIN_RESEND_TIME = 200;

    private static LeyWebClient instance;
    private static World currentWorld;
    private Map<Triple<Integer, Integer, Integer>, Long> pendingRequests = new HashMap<>();

    public static LeyWebClient getInstanceFor(World world) {
        if (instance == null || currentWorld != world)
            instance = new LeyWebClient();
        currentWorld = world;
        return instance;
    }

    public void processDataPacket(LeyWebDataPacket packet) {
        int groupX = packet.getGroupX();
        int groupZ = packet.getGroupZ();

        if (packet.getDimension() != currentWorld.provider.getDimension())
            return;

        LeyNodeGroup group = getNodeGroup(groupX, groupZ);

        for (LeyNode node : packet.getNodes()) {
            group.add(node.id);
            nodes.put(node.id, node);
        }

        for (LeyLine leyLine : packet.getEdges()) {
            if (leyLine != null)
                _newLeyLine(leyLine.getSource(), leyLine.getTarget(), leyLine.getFlowAmount());
        }

        pendingRequests.remove(Triple.of(groupX, groupZ, packet.getDimension()));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        throw new UnsupportedOperationException("Client-side ley web can't be read from NBT!");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        throw new UnsupportedOperationException("Client-side ley web can't be written to NBT!");
    }

    public void queueWebForFetching(int centerChunkX, int centerChunkZ, int radius) {
        Set<Pair<Integer, Integer>> groupsToFetch = new HashSet<>();
        long now = currentWorld.getTotalWorldTime();
        int dim = currentWorld.provider.getDimension();
        for (int chunkX = centerChunkX - radius; chunkX <= centerChunkX + radius; chunkX++) {
            for (int chunkZ = centerChunkZ - radius; chunkZ <= centerChunkZ + radius; chunkZ++) {
                int groupX = Math.floorDiv(chunkX, 3);
                int groupZ = Math.floorDiv(chunkZ, 3);
                if (getNodeGroup(groupX, groupZ).isEmpty() && canRequestWebData(groupX, groupZ, dim, now))
                    groupsToFetch.add(Pair.of(groupX, groupZ));
            }
        }
        int dimensionID = currentWorld.provider.getDimension();
        for (Pair<Integer, Integer> groupCoords : groupsToFetch) {
            LeyWebPacketBase message = new RequestLeyWebDataPacket()
                    .setDimension(dimensionID)
                    .setCoords(groupCoords.getLeft(), groupCoords.getRight());
            PacketHandler.channel.sendToServer(message);
            pendingRequests.put(Triple.of(groupCoords.getLeft(), groupCoords.getRight(), dimensionID), now);
        }
    }

    // Make sure we don't spam the server with requests
    private boolean canRequestWebData(int groupX, int groupZ, int dim, long now) {
        Long reqTime = pendingRequests.get(Triple.of(groupX, groupZ, dim));
        return reqTime == null || now - reqTime > MIN_RESEND_TIME;
    }
}
