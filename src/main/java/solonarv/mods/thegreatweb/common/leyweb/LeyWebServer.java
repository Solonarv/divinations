package solonarv.mods.thegreatweb.common.leyweb;

import com.google.common.graph.EndpointPair;
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
import java.util.stream.Stream;

public class LeyWebServer extends WorldSavedData implements ILeyWeb {

    public static final IntRange LEYLINE_POWER_RANGE = new IntRange(2000, 10000);

    public static final int GROUP_SIZE_CHUNKS = 3;
    public static final int GROUP_SIZE_BLOCKS = GROUP_SIZE_CHUNKS * 16;

    private static int VERSION = 1;

    private static final String DATA_NAME = Misc.MOD_ID + "_LeyWebData";

    private Map<Integer, LeyNode> nodes;
    private Map<Integer, Collection<Integer>> adjacencyMap;

    private Map<Pair<Integer, Integer>, LeyNodeGroup> nodeGroups;
    private int nextNodeID;
    private Map<Pair<Integer, Integer>, LeyLine> leylines;

    public LeyWebServer() {
        super(DATA_NAME);
        nodes = new HashMap<>(128);
        adjacencyMap = new HashMap<>(128);
        leylines = new HashMap<>(64);
        nodeGroups = new HashMap<>(32);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        int version = nbt.getInteger("version");
        if (version != VERSION)
            throw new UnsupportedOperationException(String.format("Error while deserializing LeyWebServer data: unknown version %d", version));
        int numNodes = nbt.getInteger("nodeCount");
        nextNodeID = nbt.getInteger("nextNodeID");

        NBTTagList nbtNodes = nbt.getTagList("nodes", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtNodes) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            int nodeID = data[3];
            LeyNode node = new LeyNode(data[0], data[2], nodeID);

            nodes.put(nodeID, node);
            getNodeGroupForBlockCoords(node.getX(), node.getZ()).add(nodeID);
        }

        NBTTagList nbtEdges = nbt.getTagList("edges", Constants.NBT.TAG_INT_ARRAY);
        for (NBTBase tag : nbtEdges) {
            int[] data = ((NBTTagIntArray) tag).getIntArray();
            _newLeyLine(data[0], data[1], data[2]);
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
        for (LeyLine leyLine : leylines.values()) {
            int[] data = new int[]{leyLine.getSource(), leyLine.getTarget(), leyLine.getFlowAmount()};
            nbtEdges.appendTag(new NBTTagIntArray(data));
        }
        compound.setTag("edges", nbtEdges);

        compound.setInteger("version", VERSION);

        return compound;
    }

    public static LeyWebServer get() {
        return get(DimensionManager.getWorld(0));
    }

    public static LeyWebServer get(World world) {
        MapStorage storage = Objects.requireNonNull(world.getMapStorage());

        LeyWebServer instance = (LeyWebServer) storage.getOrLoadData(LeyWebServer.class, DATA_NAME);

        if (instance == null) {
            instance = new LeyWebServer();
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

    private void newRandomLeyLine(LeyNode sourceNode, LeyNode targetNode) {
        // If the edge already exists, don't do anything
        if (areNodesConnected(sourceNode, targetNode))
            return;
        long seed = DimensionManager.getWorld(0).getSeed() ^ (173 * ((long)sourceNode.hashCode() + (long) targetNode.hashCode()));
        Random rand = new Random(seed);
        newLeyLine(sourceNode, targetNode, LEYLINE_POWER_RANGE.random(rand), rand.nextBoolean());
    }

    public void newLeyLine(LeyNode sourceNode, LeyNode targetNode, int flowAmount, boolean reverse) {
        newLeyLine( reverse ? targetNode : sourceNode, reverse ? sourceNode : targetNode, flowAmount);
    }

    private void newLeyLine(LeyNode sourceNode, LeyNode targetNode, int flowAmount) {
        _newLeyLine(sourceNode.id, targetNode.id, flowAmount);
        TheGreatWeb.logger.debug(String.format("Created ley line from %s to %s with strength %d", sourceNode, targetNode, flowAmount));
        markDirty();
    }

    /**
     * Create a new leyline, but doesn't mark the ley web for saving. Used internally when deserializing the web from NBT.
     * @param sourceNode the node ID that the ley line starts at
     * @param targetNode the node ID that the ley line points towards
     * @param flowAmount the ley line's flow strength
     */
    private void _newLeyLine(int sourceNode, int targetNode, int flowAmount) {
        leylines.put(Pair.of(sourceNode, targetNode), new LeyLine(sourceNode, targetNode, flowAmount));
        adjacencyMap.putIfAbsent(sourceNode, new HashSet<>(3));
        adjacencyMap.get(sourceNode).add(targetNode);
        adjacencyMap.putIfAbsent(targetNode, new HashSet<>(3));
        adjacencyMap.get(targetNode).add(sourceNode);
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
                    newRandomLeyLine(nodeNorth, nodeEast);
                    newRandomLeyLine(nodeNorth, nodeWest);
                    newRandomLeyLine(nodeSouth, nodeEast);
                    newRandomLeyLine(nodeSouth, nodeWest);
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
                            newRandomLeyLine(nodeNorth, nodeEast);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeSE, nodeSW, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeSE, nodeCenter);
                            newRandomLeyLine(nodeSW, nodeCenter);
                            newRandomLeyLine(nodeNW, nodeCenter);
                            break;
                        case 1: // SE edge is normal
                            newRandomLeyLine(nodeSouth, nodeEast);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSW, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE, nodeCenter);
                            newRandomLeyLine(nodeSW, nodeCenter);
                            newRandomLeyLine(nodeNW, nodeCenter);
                        case 2: // SW edge is normal
                            newRandomLeyLine(nodeSouth, nodeWest);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeNW = generateNodeOnEdge(overworld, rand, nodeNorth, nodeWest, false, false);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSE, nodeNW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE, nodeCenter);
                            newRandomLeyLine(nodeSE, nodeCenter);
                            newRandomLeyLine(nodeNW, nodeCenter);
                        case 3: // NW edge is normal
                            newRandomLeyLine(nodeNorth, nodeWest);
                            nodeNE = generateNodeOnEdge(overworld, rand, nodeNorth, nodeEast, true, false);
                            nodeSE = generateNodeOnEdge(overworld, rand, nodeSouth, nodeEast, true, true);
                            nodeSW = generateNodeOnEdge(overworld, rand, nodeSouth, nodeWest, false, true);
                            centerPos = MathUtil.geometricCenter(nodeNE, nodeSE, nodeSW);
                            nodeCenter = newLeyNode(centerPos.getX(), centerPos.getY(), centerPos.getZ());
                            newRandomLeyLine(nodeNE, nodeCenter);
                            newRandomLeyLine(nodeSE, nodeCenter);
                            newRandomLeyLine(nodeSW, nodeCenter);
                    }
                    break;
            }

            group.setNorthNode(nodeNorth.id);
            group.setEastNode(nodeEast.id);
            group.setSouthNode(nodeSouth.id);
            group.setWestNode(nodeWest.id);

            if (connectToNeighbors) {

                LeyNodeGroup groupNorth = getOrGenerateNodeGroup(groupX, groupZ - 1, false);
                newRandomLeyLine(nodeNorth, getNode(groupNorth.getSouthNode()));

                LeyNodeGroup groupEast = getOrGenerateNodeGroup(groupX + 1, groupZ, false);
                newRandomLeyLine(nodeEast, getNode(groupEast.getWestNode()));

                LeyNodeGroup groupSouth = getOrGenerateNodeGroup(groupX, groupZ + 1, false);
                newRandomLeyLine(nodeSouth, getNode(groupSouth.getNorthNode()));

                LeyNodeGroup groupWest = getOrGenerateNodeGroup(groupX - 1, groupZ, false);
                newRandomLeyLine(nodeWest, getNode(groupWest.getEastNode()));

            }
        }
        return group;
    }

    private void generateDiagonalInGroup(World overworld, Random rand, LeyNode nodeNorth, LeyNode nodeSouth, LeyNode nodeNorthComplex, LeyNode nodeSouthComplex) {
        double u, v;
        int x, y, z;

        // first, generate the normal edges
        newRandomLeyLine(nodeNorth, nodeSouthComplex);
        newRandomLeyLine(nodeSouth, nodeNorthComplex);

        // generate the north-adjacent node
        LeyNode nodeNorthInner = generateNodeOnEdge(overworld, rand, nodeNorth, nodeNorthComplex, true, false);
        // generate the south-adjacent node
        LeyNode nodeSouthInner = generateNodeOnEdge(overworld, rand, nodeSouth, nodeSouthComplex, false, true);

        // generate edges
        newRandomLeyLine(nodeNorthInner, nodeSouthInner);

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
        newRandomLeyLine(nodeU, nodeNorthInner);
        newRandomLeyLine(nodeNorthInner, nodeV);
        return nodeNorthInner;
    }

    public LeyNode getNode(int nodeID) {
        return nodes.get(nodeID);
    }

    public Collection<LeyLine> leyLinesTouchingGroup(LeyNodeGroup group) {
        return group
                .stream()
                .flatMap(this::_leyLinesAround)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<LeyNode> nodes() {
        return nodes.values();
    }

    @Override
    public Collection<LeyLine> allLeylines() {
        return leylines.values();
    }

    @Override
    public Collection<LeyLine> leyLinesAround(LeyNode node) {
        return _leyLinesAround(node.id).collect(Collectors.toList());
    }

    private Stream<LeyLine> _leyLinesAround(int nodeID) {
        return adjacencyMap
                .get(nodeID)
                .stream()
                .flatMap(otherID -> Stream.of(leylines.get(Pair.of(nodeID, otherID)), leylines.get(Pair.of(otherID,  nodeID))));
    }

    @Override
    public Collection<LeyLine> leyLinesFrom(LeyNode node) {
        return adjacencyMap
                .get(node.id)
                .stream()
                .map(otherID -> leylines.get(Pair.of(node.id, otherID)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<LeyLine> leyLinesTo(LeyNode node) {
        return adjacencyMap
                .get(node.id)
                .stream()
                .map(otherID -> leylines.get(Pair.of(otherID, node.id)))
                .collect(Collectors.toList());
    }

    @Override
    public Pair<LeyNode, LeyNode> leylineEndpoints(LeyLine leyLine) {
        return Pair.of(nodes.get(leyLine.getSource()), nodes.get(leyLine.getTarget()));
    }

    @Override
    public boolean areNodesConnected(LeyNode source, LeyNode target) {
        Collection<Integer> forward = adjacencyMap.get(source.id);
        Collection<Integer> backward = adjacencyMap.get(target.id);
        return (forward != null && forward.contains(target.id))
                || (backward != null && backward.contains(source.id));

    }
}
