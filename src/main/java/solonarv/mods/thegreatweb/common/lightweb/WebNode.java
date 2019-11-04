package solonarv.mods.thegreatweb.common.lightweb;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class WebNode implements INBTSerializable<NBTTagCompound> {
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

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("id", id);
        tag.setInteger("x", x);
        tag.setInteger("z", z);
        tag.setFloat("size",size);
        if (outboundConnections != null) {
            int[] outboundIds = new int[outboundConnections.length];
            for (int i = 0; i < outboundConnections.length; i++) {
                outboundIds[i] = outboundConnections[i].id;
            }
            tag.setIntArray("out", outboundIds);
        }
        int[] inboundIds = new int[inboundConnections.length];
        for (int i = 0; i < inboundConnections.length; i++) {
            inboundIds[i] = inboundConnections[i].id;
        }
        tag.setIntArray("in", inboundIds);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        id = nbt.getInteger("id");
        x = nbt.getInteger("x");
        z = nbt.getInteger("z");
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

    public static WebNode fromNBT(WebData parent, NBTTagCompound nbt) {
        WebNode node = new WebNode(parent);
        node.deserializeNBT(nbt);
        return node;
    }
}
