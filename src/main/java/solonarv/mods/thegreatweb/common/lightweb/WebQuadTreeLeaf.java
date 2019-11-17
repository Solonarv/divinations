package solonarv.mods.thegreatweb.common.lightweb;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Stream;

class WebQuadTreeLeaf extends WebQuadTree {
    private WebNode[] containedNodes;
    private WebFace[] containedFaces;


    WebQuadTreeLeaf(WebQuadTreeBranch parent, int minX, int maxX, int minZ, int maxZ, WebNode[] nodes, WebFace[] faces) {
        super(parent, minX, maxX, minZ, maxZ);
        containedNodes = nodes;
        containedFaces = faces;
    }

    @NotNull
    @Override
    public Stream<WebNode> nodesNear(int x, int z, int radius) {
        if (quickIsOutside(x, z, radius))
            return Stream.empty();

        int radiusSq = radius*radius;
        return Arrays.stream(containedNodes).filter(node -> {
            int dx = node.x - x;
            int dz = node.z - z;
            return dx*dx + dz*dz <= radiusSq;
        });
    }

    @Nonnull
    @Override
    public Stream<WebFace> facesNear(int x, int z) {
        if (quickIsOutside(x, z, maxRadius))
            return Stream.empty();

        return Arrays.stream(containedFaces).filter(face -> {
            double dx = face.circumcenterX - x;
            double dz = face.circumcenterZ - z;
            return dx*dx + dz*dz <= face.circumcircleRadiusSq;
        });
    }

    @Nonnull
    @Override
    public WebQuadTree split() {
        if ((maxX - minX) * (maxZ - minZ) < MINIMUM_AREA)
            return this;
        int midX = (maxX + minX) / 2;
        int midZ = (maxZ + minZ) / 2;
        WebQuadTreeBranch ret = new WebQuadTreeBranch(parent, minX, maxX, minZ, maxZ,
                filtered(midX+1, maxX, minZ, midZ),
                filtered(midX+1, maxX, midZ+1, maxZ),
                filtered(minX, midX, midZ+1, maxZ),
                filtered(minX, midX, minZ, minZ));
        parent.replaceMe(this, ret);
        return ret;
    }

    private WebQuadTreeLeaf filtered(int minX, int maxX, int minZ, int maxZ) {
        WebNode[] newNodes = (WebNode[]) Arrays.stream(containedNodes)
                .filter(node -> node.x >= minX && node.x <= maxX && node.z >= minZ && node.z <= maxZ)
                .toArray();
        WebFace[] newFaces = (WebFace[]) Arrays.stream(containedFaces)
                .filter(face -> face.circumcenterX >= minX && face.circumcenterX <= maxX && face.circumcenterZ >= minZ && face.circumcenterZ <= maxZ)
                .toArray();
        return new WebQuadTreeLeaf(parent, minX, maxX, minZ, maxZ, newNodes, newFaces);
    }

    @Override
    public double getMaxRadius() {
        return maxRadius;
    }

    @Override
    public boolean addNode(WebNode node) {
        if (node.x < minX || node.x > maxX || node.z < minZ || node.z > maxZ)
            return false;
        containedNodes = Arrays.copyOf(containedNodes, containedNodes.length+1);
        containedNodes[containedNodes.length-1] = node;
        if (containedNodes.length > MAX_NODE_COUNT_BEFORE_SPLIT) split();
        return true;
    }

    @Override
    public boolean addFace(WebFace face) {
        if (face.circumcenterX < minX || face.circumcenterX > maxX || face.circumcenterZ < minZ || face.circumcenterZ > maxZ)
            return false;
        containedFaces = Arrays.copyOf(containedFaces, containedFaces.length+1);
        containedFaces[containedFaces.length-1] = face;
        if (containedFaces.length > MAX_FACE_COUNT_BEFORE_SPLIT) split();
        return true;
    }
}
