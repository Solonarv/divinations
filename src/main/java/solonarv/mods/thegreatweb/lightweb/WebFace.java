package solonarv.mods.thegreatweb.lightweb;

import net.minecraft.nbt.CompoundTag;

import java.util.Arrays;

public class WebFace {
    WebNode node1, node2, node3;

    // describe the circumcircle
    double circumcenterX, circumcenterZ, circumcircleRadius, circumcircleRadiusSq;
    private WebData parent;

    public WebFace(WebData parent){

        this.parent = parent;
    }

    public WebFace(WebNode node1, WebNode node2, WebNode node3) {
        WebNode[] vals = new WebNode[]{node1, node2, node3};
        Arrays.sort(vals, (x, y) -> {
            if (x == y) return 0;

            int fc = Float.compare(x.size, y.size);
            if (fc != 0) return fc;

            int xc = Integer.compare(x.x, y.x);
            if (xc != 0) return xc;

            return Integer.compare(x.z, y.z);
        });

        this.node1 = vals[0];
        this.node2 = vals[1];
        this.node3 = vals[2];

        calculateCircumcircle();
    }

    public static WebFace fromNBT(WebData parent, CompoundTag nbt) {
        WebFace ret = new WebFace(parent);
        ret.fromTag(nbt);
        return ret;
    }

    // Calculate the circumcircle of this triangle.
    // Method taken from https://en.wikipedia.org/wiki/Circumscribed_circle#Circumcenter_coordinates
    public void calculateCircumcircle() {

        final double x2 = node2.x - node1.x;
        final double z2 = node2.z - node1.z;
        final double x3 = node3.x - node1.x;
        final double z3 = node3.z - node1.z;

        final double d = 2 * (x2*z3 - z2*x3);
        final double magSq2 = x2*x2 + z2*z2;
        final double magSq3 = x3*x3 + z3*z3;

        final double ux = (z3 * magSq2 - z2 * magSq3) / d;
        final double uz = (x2 * magSq3 - x3 * magSq2) / d;

        circumcircleRadiusSq = ux*ux + uz*uz;
        circumcircleRadius = Math.sqrt(circumcircleRadiusSq);

        circumcenterX = ux + node1.x;
        circumcenterZ = uz + node1.z;
    }

    public CompoundTag toTag() {
        // TODO WebFace NBT de/serialization
        return null;
    }

    public void fromTag(CompoundTag nbt) {
        // TODO implement
    }
}
