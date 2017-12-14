package solonarv.mods.thegreatweb.common.leyweb;

import net.minecraft.world.storage.WorldSavedData;
import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.lib.IntRange;
import solonarv.mods.thegreatweb.common.lib.util.MathUtil;
import solonarv.mods.thegreatweb.common.lib.util.StreamUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractLeyWeb extends WorldSavedData implements ILeyWeb {
    // Constants for NBT de/serialization via WorldSavedData
    protected static int DATA_VERSION = 1;
    protected static final String DATA_NAME = Misc.MOD_ID + "_LeyWebData";

    // Configuration constants
    public static final IntRange LEYLINE_POWER_RANGE = new IntRange(2000, 10000);
    public static final int GROUP_SIZE_CHUNKS = 3;
    public static final int GROUP_SIZE_BLOCKS = GROUP_SIZE_CHUNKS * 16;

    // graph data storage
    /**
     * Map of node IDs to node objects. Contains every node that has been generated.
     */
    protected Map<Integer, LeyNode> nodes;
    /**
     * Maps each node ID to a collection containing the IDs of all adjacent nodes.
     * This is not strictly necessary, but omitting it would require iterating through
     * leyLines.entries() to find adjacent nodes.
     */
    protected Map<Integer, Collection<Integer>> adjacencyMap;
    /**
     * Maps pairs of group coordinates to node groups, allowing chunking.
     * This is used for procedural generation (each group is generated more-or-less independently, allowing
     * incremental generation), and for efficiently finding nearby nodes.
     */
    protected Map<Pair<Integer, Integer>, LeyNodeGroup> nodeGroups;
    /**
     * Maps each ley line (represented by a pair of node IDs in [source, target] order) to a LeyLine object,
     * which contains the additional data for that ley line.
     */
    protected Map<Pair<Integer, Integer>, LeyLine> leyLines;

    public AbstractLeyWeb(String dataName) {
        super(dataName);
        adjacencyMap = new HashMap<>(128);
        nodes = new HashMap<>(128);
        nodeGroups = new HashMap<>(32);
        leyLines = new HashMap<>(64);
    }

    /**
     * Retrieve a node group based on the coordinates of a contained location.
     * @param x an X block coordinate
     * @param z a Z block coordinate
     * @return the {@code LeyNodeGroup} containing the position described by (x, z)
     */
    protected LeyNodeGroup getNodeGroupForBlockCoords(int x, int z) {
        int groupX = Math.floorDiv(x, GROUP_SIZE_BLOCKS);
        int groupZ = Math.floorDiv(z, GROUP_SIZE_BLOCKS);
        return getNodeGroup(groupX, groupZ);
    }

    /**
     * Retrieve a node group based on its group coordinates. If the node group doesn't exist, returns a new, empty
     * group.
     * @param groupX  the group's X coordinate
     * @param groupZ the group's Z coordinate
     * @return the node group at (groupX, groupZ)
     */
    @Nonnull
    public LeyNodeGroup getNodeGroup(int groupX, int groupZ) {
        Pair<Integer, Integer> groupCoords = Pair.of(groupX, groupZ);
        nodeGroups.putIfAbsent(groupCoords, new LeyNodeGroup(groupX, groupZ));
        TheGreatWeb.logger.debug(String.format("Fetched node group <%d, %d>", groupX, groupZ));
        return nodeGroups.get(groupCoords);
    }

    /**
     * Retrieve a node based on its ID.
     * @param nodeID the ID of the node to fetch
     * @return the node with that ID, or null if it doesn't exist
     */
    @Nullable
    public LeyNode getNode(int nodeID) {
        return nodes.get(nodeID);
    }

    /**
     * Get all ley lines touching a group.
     * @param group a group of ley nodes
     * @return all ley lines connected to a node in {@code group}
     */
    public Collection<LeyLine> leyLinesTouchingGroup(LeyNodeGroup group) {
        return group
                .stream()
                .flatMap(this::_leyLinesAround)
                .collect(Collectors.toList());
    }

    protected void newLeyLine(LeyNode sourceNode, LeyNode targetNode, int flowAmount) {
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
    protected void _newLeyLine(int sourceNode, int targetNode, int flowAmount) {
        leyLines.put(Pair.of(sourceNode, targetNode), new LeyLine(sourceNode, targetNode, flowAmount));
        adjacencyMap.putIfAbsent(sourceNode, new HashSet<>(3));
        adjacencyMap.get(sourceNode).add(targetNode);
        adjacencyMap.putIfAbsent(targetNode, new HashSet<>(3));
        adjacencyMap.get(targetNode).add(sourceNode);
    }

    @Override
    public Collection<LeyNode> nodes() {
        return nodes.values();
    }

    @Override
    public Collection<LeyLine> allLeylines() {
        return leyLines.values();
    }

    @Override
    public Collection<LeyLine> leyLinesAround(LeyNode node) {
        return _leyLinesAround(node.id).collect(Collectors.toList());
    }

    private Stream<LeyLine> _leyLinesAround(int nodeID) {
        return getAdjacentNodeIDs(nodeID)
                .flatMap(otherID -> Stream.of(leyLines.get(Pair.of(nodeID, otherID)), leyLines.get(Pair.of(otherID,  nodeID))));
    }

    @Override
    public Collection<LeyLine> leyLinesFrom(LeyNode node) {
        return getAdjacentNodeIDs(node.id)
                .map(otherID -> leyLines.get(Pair.of(node.id, otherID)))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<LeyLine> leyLinesTo(LeyNode node) {
        return getAdjacentNodeIDs(node.id)
                .map(otherID -> leyLines.get(Pair.of(otherID, node.id)))
                .collect(Collectors.toList());
    }

    // Returns (a stream of) adjacencyMap.get(nodeID), or the empty stream if there is no entry in the adjacency map.
    private Stream<Integer> getAdjacentNodeIDs(int nodeID) {
        Collection<Integer> adj = adjacencyMap.get(nodeID);
        return adj != null ? adj.stream() : Stream.empty();
    }

    @Override
    public Pair<LeyNode, LeyNode> leylineEndpoints(LeyLine leyLine) {
        return Pair.of(nodes.get(leyLine.getSource()), nodes.get(leyLine.getTarget()));
    }

    @Override
    public EnumConnectionType areNodesConnected(LeyNode source, LeyNode target) {
        Collection<Integer> forward = adjacencyMap.get(source.id);
        if (forward != null && forward.contains(target.id))
            return EnumConnectionType.CONNECTED;

        Collection<Integer> backward = adjacencyMap.get(target.id);
        if (backward != null && backward.contains(source.id))
            return EnumConnectionType.REVERSE;
        return EnumConnectionType.NOT_CONNECTED;

    }

    /**
     * Returns a stream containing all the ley nodes in a radius around a chunk.
     * @param centerChunkX the center chunk's X coordinate
     * @param centerChunkZ the center chunk's Z coordinate
     * @param radius the radius in which to fetch nodes
     * @return a stream of ley nodes within {@code radius} of the chunk at {@code centerChunkX, centerChunkZ}
     */
    public Stream<LeyNode> nodesAround(int centerChunkX, int centerChunkZ, int radius) {
        IntRange xCoords = IntRange.around(centerChunkX, radius);
        IntRange zCoords = IntRange.around(centerChunkZ, radius);
        return StreamUtils.product(xCoords, zCoords).flatMap(this::nodesInChunk);

    }

    /**
     * Returns a stream containing all the ley nodes within a single chunk
     * @param chunkCoords the coordinates of the chunk to search, in [x, z] order
     * @return a stream of ley nodes within the chunk.
     */
    private Stream<LeyNode> nodesInChunk(Pair<Integer, Integer> chunkCoords) {
        return getNodeGroup(Math.floorDiv(chunkCoords.getLeft(), 16), Math.floorDiv(chunkCoords.getRight(), 16))
                .stream()
                .map(this::getNode)
                .filter(node -> MathUtil.inChunk(node, chunkCoords.getLeft(), chunkCoords.getLeft()));
    }
}
