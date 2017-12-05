package solonarv.mods.thegreatweb.common.leyweb;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.constants.Misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LeyWeb extends WorldSavedData {

    public static final int MAX_LEYLINE_POWER = 10000;
    public static final int MIN_LEYLINE_POWER =  2000;

    public static final int GROUP_SIZE_CHUNKS = 3;
    public static final int GROUP_SIZE_BLOCKS = GROUP_SIZE_CHUNKS * 16;

    public static final int MAX_NODES_PER_GROUP = 3;

    private static int VERSION = 1;

    private static final String DATA_NAME = Misc.MOD_ID + "_LeyWebData";

    private MutableValueGraph<Integer, LeyLineData> web;

    private BiMap<Integer, LeyNode> nodes;

    private Map<Pair<Integer, Integer>, Set<Integer>> nodeGroups;
    private int nextNodeID;

    public LeyWeb() {
        super(DATA_NAME);
        nodes = HashBiMap.create(64);
        web = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(false)
                .expectedNodeCount(64)
                .nodeOrder(ElementOrder.insertion())
                .build();
        nodeGroups = new HashMap(32 / 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int version = nbt.getInteger("version");
        if (version != VERSION)
            throw new UnsupportedOperationException(String.format("Error while deserializing LeyWeb data: unknown version %d", version));
        int numNodes = nbt.getInteger("nodeCount");
        nextNodeID = nbt.getInteger("nextNodeID");

        nodes = HashBiMap.create(numNodes);
        web = ValueGraphBuilder
                .directed()
                .allowsSelfLoops(false)
                .expectedNodeCount(numNodes)
                .nodeOrder(ElementOrder.insertion())
                .build();
        nodeGroups = new HashMap(numNodes / 3);

        NBTTagList nbtNodes = nbt.getTagList("nodes", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtNodes) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            LeyNode node = new LeyNode(data[0], data[1], data[2]);
            int nodeID = data[3];

            nodes.put(nodeID, node);
            web.addNode(nodeID);
            getNodeGroupForBlockCoords(node.getX(), node.getZ()).add(nodeID);
        }

        NBTTagList nbtEdges = nbt.getTagList("edges", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtEdges) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            web.putEdgeValue(data[0], data[1], new LeyLineData(data[2]));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("nodeCount", nodes.size());

        compound.setInteger("nextNodeID", nextNodeID);

        NBTTagList nbtNodes = new NBTTagList();
        for (Map.Entry<Integer, LeyNode> entry : nodes.entrySet()) {
            LeyNode node = entry.getValue();
            int[] data = new int[]{node.getX(), node.getY(), node.getZ(), entry.getKey()};
            nbtNodes.appendTag(new NBTTagIntArray(data));
        }
        compound.setTag("nodes", nbtNodes);

        NBTTagList nbtEdges = new NBTTagList();
        for (EndpointPair<Integer> edge : web.edges()) {
            LeyLineData connection = web.edgeValue(edge.source(), edge.target());
            int[] data = new int[]{edge.source(), edge.target(), connection.getFlowAmount()};
            nbtEdges.appendTag(new NBTTagIntArray(data));
        }
        compound.setTag("edges", nbtEdges);

        compound.setInteger("version", VERSION);

        return compound;
    }

    public static LeyWeb get(World world) {
        MapStorage storage = world.getMapStorage();

        LeyWeb instance = (LeyWeb) storage.getOrLoadData(LeyWeb.class, DATA_NAME);

        if (instance == null) {
            instance = new LeyWeb();
            storage.setData(DATA_NAME, instance);
        }

        return instance;
    }

    public boolean isGroupGenerated(int groupX, int groupZ) {
        return nodeGroups.containsKey(Pair.of(groupX, groupZ));
    }

    public int newLeyNode(int x, int y, int z) {
        int id = nextNodeID++;
        LeyNode node = new LeyNode(x, y, z);

        getNodeGroupForBlockCoords(x, z).add(id);
        nodes.put(id, node);
        web.addNode(id);

        markDirty();
        return id;
    }

    private Set<Integer> getNodeGroupForBlockCoords(int x, int z) {
        int groupX = Math.floorDiv(x, GROUP_SIZE_BLOCKS);
        int groupZ = Math.floorDiv(z, GROUP_SIZE_BLOCKS);
        return getNodeGroup(groupX, groupZ);
    }

    private Set<Integer> getNodeGroup(int groupX, int groupZ) {
        Pair<Integer, Integer> groupCoords = Pair.of(groupX, groupZ);
        nodeGroups.putIfAbsent(groupCoords, new HashSet<>(1));
        return nodeGroups.get(groupCoords);
    }

    public void createLeyLine(int sourceNode, int targetNode, int flowAmount) {
        markDirty();
        web.putEdgeValue(sourceNode, targetNode, new LeyLineData(flowAmount));
    }
}
