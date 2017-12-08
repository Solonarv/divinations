package solonarv.mods.thegreatweb.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import solonarv.mods.thegreatweb.client.helper.RenderHelper;
import solonarv.mods.thegreatweb.client.leyweb.LeyWebClient;
import solonarv.mods.thegreatweb.common.constants.Misc;
import solonarv.mods.thegreatweb.common.leyweb.LeyNode;
import solonarv.mods.thegreatweb.common.lib.util.MathUtil;

import java.util.Random;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Misc.MOD_ID)
@SideOnly(Side.CLIENT)
public class RenderLeyWeb {
    @SubscribeEvent
    public static void renderWorldLast(RenderWorldLastEvent event) {
        renderWeb(event.getContext(), event.getPartialTicks());
    }

    private static void renderWeb(RenderGlobal context, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.world;
        LeyWebClient leyWeb = LeyWebClient.getInstanceFor(world);

        int renderDistance = mc.gameSettings.renderDistanceChunks;
        EntityPlayer thePlayer = mc.player;

        int chunkX = MathUtil.floor(thePlayer.posX / 16);
        int chunkZ = MathUtil.floor(thePlayer.posZ / 16);

        leyWeb.queueWebForFetching(chunkX, chunkZ, renderDistance);

        leyWeb.nodesAround(chunkX, chunkZ, renderDistance).forEach(node -> renderNode(world, context, node, partialTicks));
    }

    private static void renderNode(World world, RenderGlobal context, LeyNode node, float partialTicks) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(node.getX(), node.getY(), node.getZ());
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05f);

        double time = world.getTotalWorldTime() + partialTicks;
        Random random = new Random(node.hashCode());
        time += random.nextInt();
        int color = random.nextInt(0xFFFFFF);


        RenderHelper.renderStar(color, 1f, 1f, 1f, (float) time, random.nextLong());

    }
}
