package solonarv.mods.thegreatweb.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import solonarv.mods.thegreatweb.common.block.ModBlocks;

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
        this.itemHandler = new ItemStackHandler();

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }

    private void addOwnSlots() {
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 30, 17));
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 9 + col * 18;
                int y = row * 18 + 70;
                this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 9 + row * 18;
            int y = 58 + 70;
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
}
