package solonarv.mods.thegreatweb.common.lightweb;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import solonarv.mods.thegreatweb.common.lib.util.NBTHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;

import static solonarv.mods.thegreatweb.common.constants.Misc.MOD_ID;

public class WebData extends WorldSavedData {

    private static final String DATA_NAME = MOD_ID + "_LeyWebData";

    public static final float MAX_CONNECT_RADIUS = 256;

    private ArrayList<WebNode> nodesById = Lists.newArrayList();
    private ArrayList<WebFace> facesById = Lists.newArrayList();

    private WebQuadTree nodeMap = WebQuadTree.newEmpty();

    private WebData() {
        super(DATA_NAME);
    }

    @Nonnull
    public static WebData get(World world) {
        MapStorage storage = world.getPerWorldStorage();
        WebData instance = (WebData) storage.getOrLoadData(WebData.class, DATA_NAME);

        if (instance == null) {
            instance = new WebData();
            storage.setData(DATA_NAME, instance);
        }

        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTHelper.deserializeListWith(nbt.getTagList("nodes", Constants.NBT.TAG_COMPOUND),
            data -> WebNode.fromNBT(this, data),
            nodesById,
            nodeMap::addNode);

        NBTHelper.deserializeListWith(nbt.getTagList("faces", Constants.NBT.TAG_COMPOUND),
            data -> WebFace.fromNBT(this, data),
            facesById,
            nodeMap::addFace);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setTag("nodes", NBTHelper.writeMany(nodesById, WebNode::serializeNBT));
        compound.setTag("faces", NBTHelper.writeMany(facesById, WebFace::serializeNBT));
        return compound;
    }

    WebNode getNodeByID(int id) {
        return id < nodesById.size() ? nodesById.get(id) : null;
    }
}
