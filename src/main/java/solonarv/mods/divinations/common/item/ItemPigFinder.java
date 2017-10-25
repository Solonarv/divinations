package solonarv.mods.divinations.common.item;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import solonarv.mods.divinations.common.core.DivinationsCreativeTab;

import java.util.List;

import solonarv.mods.divinations.common.constants.Misc;
import solonarv.mods.divinations.common.item.base.ItemMod;

import javax.annotation.Nonnull;

public class ItemPigFinder extends ItemMod {

    public static final int SEARCH_RADIUS = 32;
    private static final Vec3d aabbOffset = new Vec3d(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS);

    public ItemPigFinder() {
        super("pig_finder");
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (!worldIn.isRemote) {
            Vec3d position = playerIn.getPositionVector();

            Vec3d minCorner = position.subtract(aabbOffset);
            Vec3d maxCorner = position.add(aabbOffset);
            AxisAlignedBB box = new AxisAlignedBB(minCorner, maxCorner);

            Predicate<EntityPig> filter = EntitySelectors.withinRange(position.x, position.y, position.z, SEARCH_RADIUS);

            List<EntityPig> pigs = worldIn.getEntitiesWithinAABB(EntityPig.class, box, filter);

            for (EntityPig pig : pigs) {
                playerIn.sendMessage(new TextComponentString(pig.toString()));
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
