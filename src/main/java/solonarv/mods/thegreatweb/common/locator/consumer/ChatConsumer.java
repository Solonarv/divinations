package solonarv.mods.thegreatweb.common.locator.consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;
import solonarv.mods.thegreatweb.common.lib.util.ChatUtil;

import java.util.List;

public class ChatConsumer extends AbstractConsumer<ILocatorResult> {

    private ChatConsumer(){}

    public static final ChatConsumer instance = new ChatConsumer();

    @Override
    public void useResults(World world, Vec3d position, EntityPlayer user, List<ILocatorResult> results) {
        for (ILocatorResult result : results)
            ChatUtil.sendMessageUnlocalized(user, "Found " +  result.toString(), TextFormatting.GREEN);
    }

    @Override
    public Class<ILocatorResult> getInputClass() {
        return ILocatorResult.class;
    }
}
