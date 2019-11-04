package solonarv.mods.thegreatweb.common.lightweb;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static solonarv.mods.thegreatweb.common.constants.Misc.MOD_ID;

public class WebData extends WorldSavedData {

    public static final String DATA_NAME = MOD_ID + "_LeyWebData";

    public static final float MAX_CONNECT_RADIUS = 256;

    private ArrayList<WebNode> nodesById = new ArrayList<>();

    private WebQuadTree nodeMap ;//= new WebQuadTree();

    private WebData() {
        super(DATA_NAME);
    }

    @NotNull
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
        NBTTagList nodeData = nbt.getTagList("nodes", Constants.NBT.TAG_COMPOUND);
        int length = nodeData.tagCount();
        nodesById.ensureCapacity(length);
        for (int i = 0; i < length; i++) {
            nodesById.set(i, WebNode.fromNBT(this, nodeData.getCompoundTagAt(i)));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return null;
    }

    public WebNode getNodeByID(int id) {
        return id < nodesById.size() ? nodesById.get(id) : null;
    }
}
