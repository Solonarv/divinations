package solonarv.mods.divinations.common.locator.consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;
import solonarv.mods.divinations.common.util.ChatUtil;

import java.util.List;

public class ChatConsumer implements IConsumer<ILocatorResult>{

    private ChatConsumer(){}

    public static final ChatConsumer instance = new ChatConsumer();

    @Override
    public void useResults(World world, Vec3d position, EntityPlayer user, List<ILocatorResult> results) {
        for (ILocatorResult result : results)
            ChatUtil.sendMessageUnlocalized(user, result.toString(), TextFormatting.GREEN);
    }
}
