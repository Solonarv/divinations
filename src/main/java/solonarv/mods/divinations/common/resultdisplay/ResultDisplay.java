package solonarv.mods.divinations.common.resultdisplay;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import solonarv.mods.divinations.common.locator.base.LocatorResult;

import javax.annotation.Nonnull;
import java.util.List;

public class ResultDisplay {
    public static final IResultDisplay PLAYER_CHAT = (owner, center, result) ->
            owner.sendMessage(new TextComponentString(result.toString()));

    @Nonnull
    public static IResultDisplay fromNBT(NBTTagCompound display) {
        return PLAYER_CHAT;
    }
}
