package solonarv.mods.thegreatweb.client.leyweb;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.leyweb.AbstractLeyWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyLine;
import solonarv.mods.thegreatweb.common.leyweb.LeyNode;
import solonarv.mods.thegreatweb.common.leyweb.LeyNodeGroup;
import solonarv.mods.thegreatweb.common.network.LeyWebDataPacket;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class LeyWebClient extends AbstractLeyWeb {

    // 10 seconds
    private static final long MIN_RESEND_TIME = 200;

    private static LeyWebClient instance;
    private static World currentWorld;
    private Map<Triple<Integer, Integer, Integer>, Long> pendingRequests = new HashMap<>();

    public LeyWebClient() {
        super(DATA_NAME);
    }

    public static LeyWebClient getInstanceFor(World world) {
        if (instance == null || currentWorld != world)
            instance = new LeyWebClient();
        currentWorld = world;
        return instance;
    }

    public void processDataPacket(LeyWebDataPacket packet) {
        int groupX = packet.getGroupX();
        int groupZ = packet.getGroupZ();

        TheGreatWeb.logger.debug(String.format("Processing ley web data packet for group <%d,%d>", groupX, groupZ));

        if (packet.getDimension() == currentWorld.provider.getDimension()) {
            LeyNodeGroup group = getNodeGroup(groupX, groupZ);

            for (LeyNode node : packet.getNodes()) {
                group.add(node.id);
                nodes.put(node.id, node);
                TheGreatWeb.logger.debug("Received node: " + node);
            }

            for (LeyLine leyLine : packet.getEdges()) {
                if (leyLine == null)
                    continue;
                _newLeyLine(leyLine.getSource(), leyLine.getTarget(), leyLine.getFlowAmount());
                TheGreatWeb.logger.debug("Received ley line: " + leyLine);
            }
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
}
