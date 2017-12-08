package solonarv.mods.thegreatweb.common.leyweb;

/**
 * Describes the kind of connection between two ley nodes.
 */
public enum EnumConnectionType {
    /**
     * The nodes are connected, in the order of the arguments passed to {@link ILeyWeb#areNodesConnected(LeyNode, LeyNode)}.
     */
    CONNECTED,
    /**
     * The nodes are connected, but in the opposite direction to what is suggested by the arguments to {@link ILeyWeb#areNodesConnected(LeyNode, LeyNode)}
     */
    REVERSE,
    /**
     * The nodes are not connected.
     */
    NOT_CONNECTED;

    /**
     * Indicates whether the nodes are connected.
     * @return {@code true} if called on {@code CONNECTED} or {@code REVERSE}, false otherwise.
     */
    public boolean areConnected() {
        return this != NOT_CONNECTED;
    }
}
