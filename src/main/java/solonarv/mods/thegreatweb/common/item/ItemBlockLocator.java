package solonarv.mods.thegreatweb.common.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import solonarv.mods.thegreatweb.common.TheGreatWeb;
import solonarv.mods.thegreatweb.common.item.base.IModularLocatorItem;
import solonarv.mods.thegreatweb.common.item.base.ItemMod;
import solonarv.mods.thegreatweb.common.locator.BlockLocator;
import solonarv.mods.thegreatweb.common.locator.consumer.Consumers;
import solonarv.mods.thegreatweb.common.locator.consumer.ISimpleConsumer;
import solonarv.mods.thegreatweb.common.locator.filter.Filters;
import solonarv.mods.thegreatweb.common.locator.filter.IFilter;
import solonarv.mods.thegreatweb.common.locator.result.BlockResult;
import solonarv.mods.thegreatweb.common.locator.selector.ISelector;
import solonarv.mods.thegreatweb.common.locator.selector.Selectors;
import solonarv.mods.thegreatweb.common.lib.util.ChatUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockLocator extends ItemMod implements IModularLocatorItem {
    public ItemBlockLocator() {
        super("block_locator");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (!worldIn.isRemote) {
            // No need to read NBT for this, since there's only one instance
            BlockLocator loc = BlockLocator.getInstance();

            ItemStack stack = playerIn.getHeldItem(handIn);
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt == null) {
                ChatUtil.sendErrorMessage(playerIn, "Missing NBT tag!");
                return new ActionResult<>(EnumActionResult.FAIL, stack);
            }

            NBTTagList filterNBT = nbt.getTagList("filters", Constants.NBT.TAG_COMPOUND);

            List<IFilter<BlockResult>> filters = Filters.fromNBT(filterNBT, BlockResult.class);

            ISelector<BlockResult> selector = Selectors.fromString(nbt.getString("selector"), BlockResult.class);

            ISimpleConsumer<BlockResult> consumer = Consumers.fromString(nbt.getString("consumer"), BlockResult.class);

            Vec3d position = playerIn.getPositionVector();

            TheGreatWeb.logger.info("Locator: " + loc);
            TheGreatWeb.logger.info("Filters: " + filters);
            TheGreatWeb.logger.info("Selector: " + selector);
            TheGreatWeb.logger.info("Consumer: " + consumer);

            List<BlockResult> results = loc.findResults(worldIn, position, playerIn, filters);
            results = selector.select(worldIn, position, playerIn, results);
            consumer.useResults(worldIn, position, playerIn, results);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
