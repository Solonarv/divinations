package solonarv.mods.thegreatweb.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.gui.ContainerLocatorWorkbench;
import solonarv.mods.thegreatweb.common.lib.Util;

public class LocatorWorkbenchGui extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = Util.withModID("textures/gui/locator_workbench.png");

    public LocatorWorkbenchGui(ContainerLocatorWorkbench container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
