package solonarv.mods.thegreatweb.common.leyweb;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

public interface ILeyWeb {
    /**
     * @return all nodes contained in this web
     */
    Collection<LeyNode> nodes();

    /**
     * @return all ley lines that are part of this web
     */
    Collection<LeyLine> allLeylines();

    /**
     * Get the ley lines connected to a specific node. This is the union
     * of {@link ILeyWeb#leyLinesFrom(LeyNode)} and {@link ILeyWeb#leyLinesTo(LeyNode)}
     * @param node a ley node
     * @return all ley lines connected to the given node
     */
    Collection<LeyLine> leyLinesAround(LeyNode node);

    /**
     * Get the ley nodes pointing away from the given node.
     * @param node a ley node
     * @return all ley lines leaving this node
     */
    Collection<LeyLine> leyLinesFrom(LeyNode node);

    /**
     * Get the ley nodes pointing towards the given node.
     * @param node a ley node
     * @return all ley lines entering this node
     */
    Collection<LeyLine> leyLinesTo(LeyNode node);

    /**
     * Get the nodes that a ley line is connected to
     * @param leyLine a ley line
     * @return the ley line's endpoints, in [source, target] order
     */
    Pair<LeyNode, LeyNode> leylineEndpoints(LeyLine leyLine);

    /**
     * Check whether the given nodes are connected.
     * @param source the (hypothetical) source node
     * @param target the (hypothetical) target node
     * @return A {@link EnumConnectionType} describing the connection from source to target.
     */
    EnumConnectionType areNodesConnected(LeyNode source, LeyNode target);
}
