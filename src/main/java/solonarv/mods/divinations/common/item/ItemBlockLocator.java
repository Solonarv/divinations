package solonarv.mods.divinations.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import solonarv.mods.divinations.common.item.base.ItemMod;
import solonarv.mods.divinations.common.locator.BlockLocator;
import solonarv.mods.divinations.common.locator.consumer.Consumers;
import solonarv.mods.divinations.common.locator.consumer.ISimpleConsumer;
import solonarv.mods.divinations.common.locator.filter.Filters;
import solonarv.mods.divinations.common.locator.filter.IFilter;
import solonarv.mods.divinations.common.locator.result.BlockResult;
import solonarv.mods.divinations.common.locator.selector.ISelector;
import solonarv.mods.divinations.common.locator.selector.Selectors;
import solonarv.mods.divinations.common.util.ChatUtil;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockLocator extends ItemMod {
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

            System.out.println("Locator: " + loc);
            System.out.println("Filters: " + filters);
            System.out.println("Selector: " + selector);
            System.out.println("Consumer: " + consumer);

            List<BlockResult> results = loc.findResults(worldIn, position, playerIn, filters);
            results = selector.select(worldIn, position, playerIn, results);
            consumer.useResults(worldIn, position, playerIn, results);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
