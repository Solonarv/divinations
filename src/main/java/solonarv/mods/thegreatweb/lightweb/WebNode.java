package solonarv.mods.thegreatweb.lightweb;

import net.minecraft.nbt.CompoundTag;

public class WebNode {
    int id;
    int x, z;
    float size;
    WebNode[] outboundConnections;
    WebNode[] inboundConnections;

    private WebData parent;

    public WebNode(WebData parent) {
        this.parent = parent;
    }

    boolean isFullyGenerated() {
        return outboundConnections != null;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", id);
        tag.putInt("x", x);
        tag.putInt("z", z);
        tag.putFloat("size",size);
        if (outboundConnections != null) {
            int[] outboundIds = new int[outboundConnections.length];
            for (int i = 0; i < outboundConnections.length; i++) {
                outboundIds[i] = outboundConnections[i].id;
            }
            tag.putIntArray("out", outboundIds);
        }
        int[] inboundIds = new int[inboundConnections.length];
        for (int i = 0; i < inboundConnections.length; i++) {
            inboundIds[i] = inboundConnections[i].id;
        }
        tag.putIntArray("in", inboundIds);
        return tag;
    }

    public void fromTag(CompoundTag nbt) {
        id = nbt.getInt("id");
        x = nbt.getInt("x");
        z = nbt.getInt("z");
        size = nbt.getFloat("size");
        int[] outboundIds = nbt.getIntArray("out");
        if (outboundIds.length > 0) {
            outboundConnections = new WebNode[outboundIds.length];
            for (int i = 0; i < outboundIds.length; i++) {
                outboundConnections[i] = parent.getNodeByID(outboundIds[i]);
            }
        }
        int[] inboundIds = nbt.getIntArray("in");
        for (int i = 0; i < inboundIds.length; i++) {
            inboundConnections[i] = parent.getNodeByID(inboundIds[i]);
        }
    }

    public static WebNode fromNBT(WebData parent, CompoundTag nbt) {
        WebNode node = new WebNode(parent);
        node.fromTag(nbt);
        return node;
    }
}
