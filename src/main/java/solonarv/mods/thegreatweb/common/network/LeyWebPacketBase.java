package solonarv.mods.thegreatweb.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class LeyWebPacketBase implements IMessage {
    protected int groupX;
    protected int groupZ;

    @Override
    public void fromBytes(ByteBuf buf) {
        buf.writeInt(groupX);
        buf.writeInt(groupZ);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        groupX = buf.readInt();
        groupZ = buf.readInt();
    }

    public void setCoords(int groupX, int groupZ) {
        this.groupX = groupX;
        this.groupZ = groupZ;
    }
}
