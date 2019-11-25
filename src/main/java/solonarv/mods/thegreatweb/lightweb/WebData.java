package solonarv.mods.thegreatweb.lightweb;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.lib.util.NBTHelper;
import solonarv.mods.thegreatweb.constants.Misc;

import java.util.ArrayList;

public class WebData extends PersistentState {

    private static final String DATA_NAME = Misc.MOD_ID + "_LeyWebData";

    public static final float MAX_CONNECT_RADIUS = 256;
    private static final WebData CLIENT_DUMMY = new WebData();

    private ArrayList<WebNode> nodesById = Lists.newArrayList();
    private ArrayList<WebFace> facesById = Lists.newArrayList();

    private WebQuadTree nodeMap = WebQuadTree.newEmpty();

    private WebData() {
        super(DATA_NAME);
    }

    public static WebData get(World world) {
        if (world.isClient || !(world instanceof ServerWorld)) return CLIENT_DUMMY;
        ServerWorld worldServer = (ServerWorld) world;
        PersistentStateManager stateManager = worldServer.getPersistentStateManager();
        return stateManager.getOrCreate(WebData::new, DATA_NAME);
    }

    @Override
    public void fromTag(CompoundTag nbt) {
        NBTHelper.deserializeListWith(nbt.getList("nodes", NbtType.COMPOUND),
            data -> WebNode.fromNBT(this, data),
            nodesById,
            nodeMap::addNode);

        NBTHelper.deserializeListWith(nbt.getList("faces", NbtType.COMPOUND),
            data -> WebFace.fromNBT(this, data),
            facesById,
            nodeMap::addFace);
    }

    @Override
    public CompoundTag toTag(CompoundTag compound) {
        compound.put("nodes", NBTHelper.writeMany(nodesById, WebNode::toTag));
        compound.put("faces", NBTHelper.writeMany(facesById, WebFace::toTag));
        return compound;
    }

    WebNode getNodeByID(int id) {
        return id < nodesById.size() ? nodesById.get(id) : null;
    }
}
