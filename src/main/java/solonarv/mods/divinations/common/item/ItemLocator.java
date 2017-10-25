package solonarv.mods.divinations.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.item.base.ItemMod;
import solonarv.mods.divinations.common.locator.ILocator;
import solonarv.mods.divinations.common.locator.Locators;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemLocator extends ItemMod {
    public ItemLocator() {
        super("locator");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Nullable
    public ILocator getFinder(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("Locator", 10)) {
            return Locators.fromNBT(tag.getCompoundTag("Locator"));
        }
        return null;
    }
}
