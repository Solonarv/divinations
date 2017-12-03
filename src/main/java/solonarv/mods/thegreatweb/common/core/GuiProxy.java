package solonarv.mods.thegreatweb.common.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import solonarv.mods.thegreatweb.client.gui.LocatorWorkbenchGui;
import solonarv.mods.thegreatweb.common.block.ModBlocks;
import solonarv.mods.thegreatweb.common.gui.ContainerLocatorWorkbench;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (world.getBlockState(pos).getBlock() != ModBlocks.locatorWorkbench)
            return null;
        return new ContainerLocatorWorkbench(player.inventory, world, pos);
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (world.getBlockState(pos).getBlock() != ModBlocks.locatorWorkbench)
            return null;
        return new LocatorWorkbenchGui(new ContainerLocatorWorkbench(player.inventory, world, pos));
    }
}
