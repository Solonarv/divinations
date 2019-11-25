package solonarv.mods.thegreatweb.lib.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class ChatUtil {
    private ChatUtil(){}

    public static void sendMessageUnlocalized(PlayerEntity target, String msg, Formatting color) {
        target.sendMessage(new LiteralText(msg).formatted(color));
    }

    public static void sendErrorMessage(PlayerEntity target, String msg) {
        sendMessageUnlocalized(target, msg, Formatting.RED);
    }
}
