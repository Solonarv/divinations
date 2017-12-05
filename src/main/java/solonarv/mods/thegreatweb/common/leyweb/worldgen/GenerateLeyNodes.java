package solonarv.mods.thegreatweb.common.leyweb.worldgen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import solonarv.mods.thegreatweb.common.leyweb.LeyWeb;

import java.util.Random;

public class GenerateLeyNodes implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.isNether()) {
            // Special handling for generation triggered from the nether, as the Web should be the same
            // in each dimension and distances in the nether are 8x shorter.
            // So, if ley node generation is triggered by a nether chunk, we generate the web in all 8*8 = 64 chunks
            // it maps to in other dimensions.
            for (int dx = 0; dx < 8; dx++) {
                for (int dz = 0; dz < 8; dz ++) {
                    generateLeyNodes(chunkX * 8 + dx, chunkZ * 8 + dz, world);
                }
            }
        } else {
            generateLeyNodes(chunkX, chunkZ, world);
        }
    }

    private void generateLeyNodes(int chunkX, int chunkZ, World world) {
        int groupX = Math.floorDiv(chunkX, LeyWeb.GROUP_SIZE_CHUNKS);
        int groupZ = Math.floorDiv(chunkZ, LeyWeb.GROUP_SIZE_CHUNKS);
        generateLeyNodesInGroup(groupX, groupZ, world, true);
    }

    private void generateLeyNodesInGroup(int groupX, int groupZ, World world, boolean connectToNeighbors) {
        LeyWeb leyWeb = LeyWeb.get(world);
        if (!leyWeb.isGroupGenerated(groupX, groupZ)) {
            // combine groupX and groupZ into a long, add magic number, season with a pinch of world seed
            long seed = world.getSeed() ^ (173 * ((long)groupX << 32 + (long) groupZ));
            Random rand = new Random(seed);

            int nodesToGenerate = rand.nextInt(LeyWeb.MAX_NODES_PER_GROUP + 1);
            int[] nodeIDs = new int[nodesToGenerate];

            for (int i = 0; i < nodesToGenerate; i++) {
                int x = rand.nextInt(LeyWeb.GROUP_SIZE_BLOCKS) + groupX * LeyWeb.GROUP_SIZE_BLOCKS;
                int z = rand.nextInt(LeyWeb.GROUP_SIZE_BLOCKS) + groupZ * LeyWeb.GROUP_SIZE_BLOCKS;
                int y = world.getHeight(x, z);
                nodeIDs[i] = leyWeb.newLeyNode(x, y, z);
            }

            // internalConnections ranges over [1, nodesToGenerate - 1] (inclusive)
            if (nodesToGenerate > 1) {
                int internalConnections = rand.nextInt(nodesToGenerate - 1) + 1;
                for (int i = 0; i < internalConnections; i++) {
                    int flow = rand.nextInt(LeyWeb.MAX_LEYLINE_POWER - LeyWeb.MIN_LEYLINE_POWER) + LeyWeb.MIN_LEYLINE_POWER;
                    leyWeb.createLeyLine(nodeIDs[i], nodeIDs[i + 1], flow);
                }
            }

            if (!connectToNeighbors)
                return;

            // Make sure nodes in neighboring groups have generated properly
            for (int dx = -1; dx < 2; dx++) {
                for (int dz = -1; dz < 2; dz++) {
                    if (dx != 0 || dz != 0) {
                        generateLeyNodesInGroup(groupX + dx, groupZ + dz, world, false);
                    }
                }
            }

            // TODO connect to nodes from adjacent groups
        }
    }
}
