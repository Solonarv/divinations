package solonarv.mods.thegreatweb.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import solonarv.mods.thegreatweb.common.TheGreatWeb;

public class BlockLocatorWorkbench extends Block {
    private static final int GUI_ID = 1;

    public BlockLocatorWorkbench() {
        super(Material.WOOD);
        setUnlocalizedName("blockLocatorWorkbench");
        setRegistryName("locator_workbench");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            playerIn.openGui(TheGreatWeb.instance, GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

}
