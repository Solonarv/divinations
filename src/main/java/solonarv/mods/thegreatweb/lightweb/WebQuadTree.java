package solonarv.mods.thegreatweb.lightweb;

import java.util.stream.Stream;

/** A simplistic specialized quad-tree for storing web nodes & faces.
 *
 */
public abstract class WebQuadTree {

    static final int MINIMUM_AREA = 16;
    static final int MAX_NODE_COUNT_BEFORE_SPLIT = 256;
    static final int MAX_FACE_COUNT_BEFORE_SPLIT = 256;
    private static final int MAX_WORLD_SIZE = 30_000_000;

    WebQuadTreeBranch parent;

    int minX;
    int minZ;
    int maxX;
    int maxZ;

    double maxRadius;

    public static WebQuadTree newEmpty() {
        return new WebQuadTreeLeaf(null, -MAX_WORLD_SIZE, MAX_WORLD_SIZE, -MAX_WORLD_SIZE, MAX_WORLD_SIZE, new WebNode[]{}, new WebFace[]{});
    }

    WebQuadTree(WebQuadTreeBranch parent, int minX, int maxX, int minZ, int maxZ){
        this.parent = parent;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public abstract Stream<WebNode> nodesNear(int x, int z, int radius);

    public abstract Stream<WebFace> facesNear(int x, int z);

    public abstract WebQuadTree split();

    public abstract double getMaxRadius();

    boolean quickIsOutside(int x, int z, double radius) {
        return !((x - radius <= maxX || x + radius >= minX)
                    && (z - radius <= maxZ || z + radius >= minZ));
    }

    public abstract boolean addNode(WebNode node);

    public abstract boolean addFace(WebFace face);

}
