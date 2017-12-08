package solonarv.mods.thegreatweb.common.lib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil {
    private ChatUtil(){}

    public static void sendMessageUnlocalized(EntityPlayer target, String msg, TextFormatting color) {
        target.sendMessage(new TextComponentString(msg).setStyle(new Style().setColor(color)));
    }

    public static void sendErrorMessage(EntityPlayer target, String msg) {
        sendMessageUnlocalized(target, msg, TextFormatting.RED);
    }
}
