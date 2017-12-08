package solonarv.mods.thegreatweb.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class LeyWebPacketBase implements IMessage {
    protected int groupX;
    protected int groupZ;
    protected int dimension;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(groupX);
        buf.writeInt(groupZ);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        groupX = buf.readInt();
        groupZ = buf.readInt();
    }

    public LeyWebPacketBase setCoords(int groupX, int groupZ) {
        this.groupX = groupX;
        this.groupZ = groupZ;
        return this;
    }

    public int getGroupX() {
        return groupX;
    }

    public int getGroupZ() {
        return groupZ;
    }

    public int getDimension() {
        return dimension;
    }

    public LeyWebPacketBase setDimension(int dimension) {
        this.dimension = dimension;
        return this;
    }
}
