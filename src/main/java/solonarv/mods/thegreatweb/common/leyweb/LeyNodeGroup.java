package solonarv.mods.thegreatweb.common.leyweb;


import solonarv.mods.thegreatweb.common.lib.IntRange;

import javax.annotation.Nonnull;
import java.util.*;

public class LeyNodeGroup extends HashSet<Integer> {
    private final int x;
    private final int z;

    private int northNode, southNode, eastNode, westNode;

    public LeyNodeGroup(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Set<LeyNode> getNodes(LeyWebServer web) {
        Set<LeyNode> result = new HashSet<>(size());
        for (int nodeID : this) {
            LeyNode node = web.getNode(nodeID);
            if (node != null)
                result.add(node);
        }
        return result;
    }

    public int getNorthNode() {
        return northNode;
    }

    public void setNorthNode(int northNode) {
        this.northNode = northNode;
    }

    public int getSouthNode() {
        return southNode;
    }

    public void setSouthNode(int southNode) {
        this.southNode = southNode;
    }

    public int getEastNode() {
        return eastNode;
    }

    public void setEastNode(int eastNode) {
        this.eastNode = eastNode;
    }

    public int getWestNode() {
        return westNode;
    }

    public void setWestNode(int westNode) {
        this.westNode = westNode;
    }

    public enum ConnectionStyle {
        SIMPLE(50, "simple square"),
        DIAGONAL(40, "square with a pseudo-diagonal"),
        TRIANGULAR(25, "square with three edges connecting to the center");

        private final int weight;
        private final String description;

        private static IntRange weightRange = new IntRange(0, Arrays.stream(values()).mapToInt(ConnectionStyle::getWeight).sum() - 1);

        ConnectionStyle(int weight, String description) {
            this.weight = weight;
            this.description = description;
        }

        public int getWeight() {
            return weight;
        }

        public String getDescription() {
            return description;
        }

        @Nonnull
        public static ConnectionStyle randomStyle(Random rng) {
            int index = weightRange.random(rng);
            int passed = 0;
            for (ConnectionStyle style : values()) {
                passed += style.weight;
                if (passed > index)
                    return style;
            }
            // Not reachable
            return SIMPLE;
        }
    }
}
