package solonarv.mods.thegreatweb.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import solonarv.mods.thegreatweb.common.block.ModBlocks;
import solonarv.mods.thegreatweb.common.item.base.IModularLocatorItem;

import javax.annotation.Nonnull;

public class ContainerLocatorWorkbench extends Container {
    private final InventoryPlayer playerInventory;
    private final World world;
    private final BlockPos position;
    private final EntityPlayer player;
    private final IItemHandlerModifiable itemHandler;

    public ContainerLocatorWorkbench(InventoryPlayer playerInventory, World world, BlockPos position) {
        this.playerInventory = playerInventory;
        this.world = world;
        this.position = position;
        this.player = playerInventory.player;
        this.itemHandler = new ItemStackHandler(){
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addOwnSlots() {
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 31, 18){
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return super.isItemValid(stack) && stack.getItem() instanceof IModularLocatorItem;
            }
        });
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 142;
            this.addSlotToContainer(new Slot(playerInventory, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return (this.world.getBlockState(this.position).getBlock() == ModBlocks.locatorWorkbench)
                && (playerIn.getDistanceSq((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D);
    }

    public void onLocatorChanged() {

    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote)
        {
            this.clearContainer(playerIn, this.world, this.itemHandler);
        }
    }

    protected void clearContainer(EntityPlayer playerIn, World worldIn, IItemHandler itemHandler) {
        if (!playerIn.isEntityAlive() || playerIn instanceof EntityPlayerMP && ((EntityPlayerMP)playerIn).hasDisconnected()) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                playerIn.dropItem(itemHandler.extractItem(i, itemHandler.getSlotLimit(i), false), false);
            }
        } else {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                playerIn.inventory.placeItemBackInInventory(worldIn, itemHandler.extractItem(i, itemHandler.getSlotLimit(i), false));
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < itemHandler.getSlots()) {
                if (!this.mergeItemStack(itemstack1, itemHandler.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, itemHandler.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack != null ? itemstack : ItemStack.EMPTY;
    }
}
