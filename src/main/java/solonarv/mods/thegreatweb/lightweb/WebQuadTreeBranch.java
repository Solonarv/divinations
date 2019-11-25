package solonarv.mods.thegreatweb.lightweb;

import solonarv.mods.thegreatweb.lib.util.MathUtil;

import java.util.stream.Stream;

class WebQuadTreeBranch extends WebQuadTree {
    private WebQuadTree subtreeNE;
    private WebQuadTree subtreeSE;
    private WebQuadTree subtreeSW;
    private WebQuadTree subtreeNW;

    WebQuadTreeBranch(WebQuadTreeBranch parent, int minX, int maxX, int minZ, int maxZ, WebQuadTree subtreeNE, WebQuadTree subtreeSE, WebQuadTree subtreeSW, WebQuadTree subtreeNW) {
        super(parent, minX, maxX, minZ, maxZ);
        this.subtreeNE = subtreeNE;
        this.subtreeSE = subtreeSE;
        this.subtreeSW = subtreeSW;
        this.subtreeNW = subtreeNW;
    }

    @Override
    public Stream<WebNode> nodesNear(int x, int z, int radius) {
        if (quickIsOutside(x, z, radius))
            return Stream.empty();
        return Stream.of(subtreeNE, subtreeSE, subtreeSW, subtreeNW)
                .flatMap(child -> child.nodesNear(x, z, radius));
    }

    @Override
    public Stream<WebFace> facesNear(int x, int z) {
        if (quickIsOutside(x, z, maxRadius))
            return Stream.empty();
        return Stream.of(subtreeNE, subtreeSE, subtreeSW, subtreeNW)
                .flatMap(child -> child.facesNear(x, z));
    }

    @Override
    public WebQuadTree split() {
        return this;
    }

    @Override
    public double getMaxRadius() {
        return MathUtil.maxMany(subtreeNE.getMaxRadius(), subtreeSE.getMaxRadius(), subtreeSW.getMaxRadius(), subtreeNW.getMaxRadius());
    }

    @Override
    public boolean addNode(WebNode node) {
        if (node.x < minX || node.x > maxX || node.z < minZ || node.z > maxZ)
            return false;
        return Stream.of(subtreeNE, subtreeSE, subtreeSW, subtreeNW)
                .anyMatch(child -> child.addNode(node));
    }

    @Override
    public boolean addFace(WebFace face) {
        if (face.circumcenterX < minX || face.circumcenterX > maxX || face.circumcenterZ < minZ || face.circumcenterZ > maxZ)
            return false;
        return Stream.of(subtreeNE, subtreeSE, subtreeSW, subtreeNW)
                .anyMatch(child -> child.addFace(face));
    }

    void replaceMe(WebQuadTree oldChild, WebQuadTree newChild) {
        if (oldChild == subtreeNE) {
            subtreeNE = newChild;
            return;
        }
        if (oldChild == subtreeSE) {

            subtreeSE = newChild;
            return;
        }
        if (oldChild == subtreeSW) {
            subtreeSW = newChild;
            return;
        }
        if (oldChild == subtreeNW) subtreeNW = newChild;
    }
}
