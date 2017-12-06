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
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.lib.IntRange;
import solonarv.mods.thegreatweb.common.util.MathUtil;

import java.util.*;
import java.util.stream.Collectors;

public class LeyWeb extends WorldSavedData {

    public static final IntRange LEYLINE_POWER_RANGE = new IntRange(2000, 10000);

    public static final int GROUP_SIZE_CHUNKS = 3;
    public static final int GROUP_SIZE_BLOCKS = GROUP_SIZE_CHUNKS * 16;

    private static int VERSION = 1;

    private static final String DATA_NAME = Misc.MOD_ID + "_LeyWebData";

    private MutableValueGraph<Integer, LeyLine> web;

    private BiMap<Integer, LeyNode> nodes;

    private Map<Pair<Integer, Integer>, LeyNodeGroup> nodeGroups;
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
        nodeGroups = new HashMap<>(32 / 3);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int version = nbt.getInteger("version");
        if (version != VERSION)
            throw new UnsupportedOperationException(String.format("Error while deserializing LeyWeb data: unknown version %d", version));
        int numNodes = nbt.getInteger("nodeCount");
        nextNodeID = nbt.getInteger("nextNodeID");

        NBTTagList nbtNodes = nbt.getTagList("nodes", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtNodes) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            int nodeID = data[3];
            LeyNode node = new LeyNode(data[0], data[2], nodeID);

            nodes.put(nodeID, node);
            web.addNode(nodeID);
            getNodeGroupForBlockCoords(node.getX(), node.getZ()).add(nodeID);
        }

        NBTTagList nbtEdges = nbt.getTagList("edges", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtEdges) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            web.putEdgeValue(data[0], data[1], new LeyLine(data[0], data[1], data[2]));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        TheGreatWeb.logger.debug("Saving Ley Web data...");

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
            LeyLine connection = web.edgeValue(edge.source(), edge.target());
            int[] data = new int[]{edge.source(), edge.target(), connection.getFlowAmount()};
            nbtEdges.appendTag(new NBTTagIntArray(data));
        }
        compound.setTag("edges", nbtEdges);

        compound.setInteger("version", VERSION);

        return compound;
    }

    public static LeyWeb get() {
        return get(DimensionManager.getWorld(0));
    }

    public static LeyWeb get(World world) {
        MapStorage storage = Objects.requireNonNull(world.getMapStorage());

        LeyWeb instance = (LeyWeb) storage.getOrLoadData(LeyWeb.class, DATA_NAME);

        if (instance == null) {
            instance = new LeyWeb();
            storage.setData(DATA_NAME, instance);
        }

        return instance;
    }

    public boolean isGroupGenerated(int groupX, int groupZ) {
        return !getNodeGroup(groupX, groupZ).isEmpty();
    }

    public LeyNode newLeyNode(int x, int y, int z) {
        int id = nextNodeID++;
        LeyNode node = new LeyNode(x, z, id);

        getNodeGroupForBlockCoords(x, z).add(id);
        nodes.put(id, node);
        web.addNode(id);

        markDirty();
        return node;
    }

    private Set<Integer> getNodeGroupForBlockCoords(int x, int z) {
        int groupX = Math.floorDiv(x, GROUP_SIZE_BLOCKS);
        int groupZ = Math.floorDiv(z, GROUP_SIZE_BLOCKS);
        return getNodeGroup(groupX, groupZ);
    }

    public LeyNodeGroup getNodeGroup(int groupX, int groupZ) {
        Pair<Integer, Integer> groupCoords = Pair.of(groupX, groupZ);
        nodeGroups.putIfAbsent(groupCoords, new LeyNodeGroup(groupX, groupZ));
        TheGreatWeb.logger.debug(String.format("Fetched node group <%d, %d>", groupX, groupZ));
        return nodeGroups.get(groupCoords);
    }

    private void newRandomLeyLine(int sourceNode, int targetNode) {
        // If the edge already exists, don't do anything
        if (web.adjacentNodes(sourceNode).contains(targetNode))
            return;
        LeyNode u = getNode(sourceNode);
        LeyNode v = getNode(targetNode);
        if (u == null || v == null)
            throw new IllegalStateException("Attempted to add a ley line from a node that doesn't exist!");
        long seed = DimensionManager.getWorld(0).getSeed() ^ (173 * ((long)u.hashCode() << 32 + (long) v.hashCode()));
        Random rand = new Random(seed);
        newLeyLine(sourceNode, targetNode, LEYLINE_POWER_RANGE.random(rand), rand.nextBoolean());
    }

    public void newLeyLine(int sourceNode, int targetNode, int flowAmount, boolean reverse) {
        newLeyLine( reverse ? targetNode : sourceNode, reverse ? sourceNode : targetNode, flowAmount);
    }

    private void newLeyLine(int sourceNode, int targetNode, int flowAmount) {
        web.putEdgeValue(sourceNode, targetNode, new LeyLine(sourceNode, targetNode, flowAmount));
        TheGreatWeb.logger.debug(String.format("Created ley line from %d to %d with strength %d", sourceNode, targetNode, flowAmount));
        markDirty();
    }

    public LeyNodeGroup getOrGenerateNodeGroup(int groupX, int groupZ, boolean connectToNeighbors) {
        LeyNodeGroup group = getNodeGroup(groupX, groupZ);
        // If this group isn't generated yet, randomly place nodes based on the world seed
        if (group.isEmpty()) {
            // Get the overworld
            World overworld = DimensionManager.getWorld(0);

            // combine groupX and groupZ into a long, add magic number, season with a pinch of world seed
            long seed = overworld.getSeed() ^ (173 * ((long)groupX << 32 + (long) groupZ));
            Random rand = new Random(seed);

            // For use below
            int x, y, z, dist;
            int offsetX = groupX * GROUP_SIZE_BLOCKS;
            int offsetZ = groupZ * GROUP_SIZE_BLOCKS;
            int center = GROUP_SIZE_BLOCKS / 2;
            IntRange range = new IntRange(0, GROUP_SIZE_BLOCKS - 1);

            // Generate the node in the NORTH (lower Z) quadrant
            x = range.random(rand);
            dist = Math.abs(x - center);
            z = range.withMax(dist).random(rand);
            y = overworld.getHeight(x, z);
            LeyNode nodeNorth = newLeyNode(offsetX + x, y, offsetZ + z);

            // Generate the node in the SOUTH (higher offsetZ + z) quadrant
            x = range.random(rand);
            dist = Math.abs(x - center);
            z = range.withMin(dist).random(rand);
            y = overworld.getHeight(offsetX + x, offsetZ + z);
            LeyNode nodeSouth = newLeyNode(offsetX + x, y, offsetZ + z);

            // Generate the node in the WEST (lower X) quadrant
            z = range.random(rand);
            dist = Math.abs(z - center);
            x = range.withMax(dist).random(rand);
            y = overworld.getHeight(offsetX + x, offsetZ + z);
            LeyNode nodeWest = newLeyNode(offsetX + x, y, offsetZ + z);

            // Generate the node in the EAST (higher X) quadrant
            z = range.random(rand);
            dist = Math.abs(z - center);
            x = range.withMax(dist).random(rand);
            y = overworld.getHeight(offsetX + x, offsetZ + z);
            LeyNode nodeEast = newLeyNode(offsetX + x, y, offsetZ + z);

            double u, v;
            // Do internal connections, in one of several possible styles
            switch (LeyNodeGroup.ConnectionStyle.randomStyle(rand)) {

                case SIMPLE:
                    newRandomLeyLine(nodeNorth.id, nodeEast.id);
                    newRandomLeyLine(nodeNorth.id, nodeWest.id);
                    newRandomLeyLine(nodeSouth.id, nodeEast.id);
                    newRandomLeyLine(nodeSouth.id, nodeWest.id);
                    break;
                case DIAGONAL:
                    // randomly pick NE-SW or NW-SE diagonal
                    if (rand.nextBoolean()) {
                        // NE-SW diagonal was picked
                        generateDiagonalInGroup(overworld, rand, nodeNorth, nodeSouth, nodeEast, nodeWest);

                    } else {
                        // NW-SE diagonal was picked
                        generateDiagonalInGroup(overworld, rand, nodeNorth, nodeSouth, nodeWest, nodeEast);
                    }
                    break;
                case TRIANGULAR:
                    LeyNode nodeNE, nodeSE, nodeSW, nodeNW, nodeCenter;
                    Vec3i centerPos;
                    // randomly pick one of the four edges to leave as normal
                    switch (rand.nextInt(4)){
                        case 0: // NE edge is normal
                            newRandomLeyLine(nodeNorth.id, nodeEast.id);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeSE, nodeSW, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeSE.id, nodeCenter.id);
                            newRandomLeyLine(nodeSW.id, nodeCenter.id);
                            newRandomLeyLine(nodeNW.id, nodeCenter.id);
                            break;
                        case 1: // SE edge is normal
                            newRandomLeyLine(nodeSouth.id, nodeEast.id);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSW, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE.id, nodeCenter.id);
                            newRandomLeyLine(nodeSW.id, nodeCenter.id);
                            newRandomLeyLine(nodeNW.id, nodeCenter.id);
                        case 2: // SW edge is normal
                            newRandomLeyLine(nodeSouth.id, nodeWest.id);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSE, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE.id, nodeCenter.id);
                            newRandomLeyLine(nodeSE.id, nodeCenter.id);
                            newRandomLeyLine(nodeNW.id, nodeCenter.id);
                        case 3: // NW edge is normal
                            newRandomLeyLine(nodeNorth.id, nodeWest.id);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSE, nodeSW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE.id, nodeCenter.id);
                            newRandomLeyLine(nodeSE.id, nodeCenter.id);
                            newRandomLeyLine(nodeSW.id, nodeCenter.id);
                    }
                    break;
            }

            group.setNorthNode(nodeNorth.id);
            group.setEastNode(nodeEast.id);
            group.setSouthNode(nodeSouth.id);
            group.setWestNode(nodeWest.id);

            if (connectToNeighbors) {

                LeyNodeGroup groupNorth = getOrGenerateNodeGroup(groupX, groupZ - 1, false);
                newRandomLeyLine(nodeNorth.id, groupNorth.getSouthNode());

                LeyNodeGroup groupEast = getOrGenerateNodeGroup(groupX + 1, groupZ, false);
                newRandomLeyLine(nodeEast.id, groupEast.getWestNode());

                LeyNodeGroup groupSouth = getOrGenerateNodeGroup(groupX, groupZ + 1, false);
                newRandomLeyLine(nodeSouth.id, groupSouth.getNorthNode());

                LeyNodeGroup groupWest = getOrGenerateNodeGroup(groupX - 1, groupZ, false);
                newRandomLeyLine(nodeWest.id, groupWest.getEastNode());

            }
        }
        return group;
    }

    private void generateDiagonalInGroup(World overworld, Random rand, LeyNode nodeNorth, LeyNode nodeSouth, LeyNode nodeNorthComplex, LeyNode nodeSouthComplex) {
        double u, v;
        int x, y, z;

        // first, generate the normal edges
        newRandomLeyLine(nodeNorth.id, nodeSouthComplex.id);
        newRandomLeyLine(nodeSouth.id, nodeNorthComplex.id);

        // generate the north-adjacent node
        LeyNode nodeNorthInner = generateNodeOnEdge(overworld, rand, nodeNorth, nodeNorthComplex, true, false);
        // generate the south-adjacent node
        LeyNode nodeSouthInner = generateNodeOnEdge(overworld, rand, nodeSouth, nodeSouthComplex, false, true);

        // generate edges
        newRandomLeyLine(nodeNorthInner.id, nodeSouthInner.id);

    }

    private LeyNode generateNodeOnEdge(World overworld, Random rand, LeyNode nodeU, LeyNode nodeV, boolean eastHalf, boolean southHalf) {
        double u, v;
        int x, y, z;
        // first, local coordinates
        u = rand.nextDouble();
        v = rand.nextDouble();
        // normalize the local coords to make sure they're in the {(1,0), (0,0), (0,1)} triangle
        if (u + v > 1) {
            if (u > v)
                u -= v;
            else
                v -= u;
        }
        // Flip to a different triangle if necessary
        if (!eastHalf)
            u = 1 - u;
        if (!southHalf)
            v = 1 - v;
        // convert to world coords
        x = new IntRange(nodeU.getX(), nodeV.getX()).weightedAverage(u);
        z = new IntRange(nodeU.getZ(), nodeV.getZ()).weightedAverage(v);
        y = overworld.getHeight(x, z);
        LeyNode nodeNorthInner = newLeyNode(x, y, z);
        newRandomLeyLine(nodeU.id, nodeNorthInner.id);
        newRandomLeyLine(nodeNorthInner.id, nodeV.id);
        return nodeNorthInner;
    }

    public LeyNode getNode(int nodeID) {
        return nodes.get(nodeID);
    }

    public Set<LeyLine> getEdgesAroundNode(int nodeID) {
        // Why the FUCK does guava not have a method for this
        return web
                .edges()
                .stream()
                .filter((pair -> pair.target() == nodeID || pair.source() == nodeID))
                .map(pair -> getLeyLine(pair.source(), pair.target()))
                .collect(Collectors.toSet());
    }

    public Set<LeyLine> getEdgesTouchingGroup(LeyNodeGroup group) {
        return web
                .edges()
                .stream()
                .filter(pair -> group.contains(pair.target()) || group.contains(pair.source()))
                .map(pair -> getLeyLine(pair.source(), pair.target()))
                .collect(Collectors.toSet());
    }

    public LeyLine getLeyLine(int sourceID, int targetID) {
        return web.edgeValue(sourceID, targetID);
    }
}
