package solonarv.mods.divinations.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import solonarv.mods.divinations.common.Divinations;
import solonarv.mods.divinations.common.item.base.ItemMod;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;
import solonarv.mods.divinations.common.locator.Locators;
import solonarv.mods.divinations.common.resultdisplay.IResultDisplay;
import solonarv.mods.divinations.common.resultdisplay.ResultDisplay;
import solonarv.mods.divinations.common.selector.Selectors;
import solonarv.mods.divinations.common.selector.base.ISelector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemLocator extends ItemMod {
    public ItemLocator() {
        super("locator");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        Divinations.proxy.performLocate(worldIn, playerIn, playerIn.getPositionVector(), getLocator(heldItem), getSelector(heldItem), getResultDisplay(heldItem));
        return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
    }

    @Nonnull
    public IConcreteLocator getLocator(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("Locator", Constants.NBT.TAG_COMPOUND)) {
            return Locators.fromNBT(tag.getCompoundTag("Locator"));
        }
        return Locators.EMPTY;
    }

    @Nonnull
    public ISelector getSelector(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("Selector", Constants.NBT.TAG_COMPOUND))
            return Selectors.fromNBT(tag.getCompoundTag("Selector"));
        return Selectors.ALL;
    }

    @Nonnull
    public IResultDisplay getResultDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("Display", Constants.NBT.TAG_COMPOUND))
            return ResultDisplay.fromNBT(tag.getCompoundTag("Display"));
        return ResultDisplay.PLAYER_CHAT;
    }
}
