package com.the_blood_knight.techrot.common.world;

import com.the_blood_knight.techrot.common.TRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenRotOre implements IWorldGenerator {

    private final IBlockState ORE = TRegistry.ROTPLATE_ORE.getDefaultState();

    @Override
    public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        if (world.provider.getDimension() == 0) {  // Overworld
            generateOre(ORE, world, rand, chunkX * 16, chunkZ * 16,
                    6,  // vein size
                    12, // veins per chunk
                    4,  // min height
                    25  // max height
            );
        }
    }

    private void generateOre(IBlockState ore, World world, Random rand, int x, int z,
                             int veinSize, int chances, int minY, int maxY) {

        int heightDiff = maxY - minY;
        for (int i = 0; i < chances; i++) {
            int posX = x + rand.nextInt(16);
            int posY = minY + rand.nextInt(heightDiff);
            int posZ = z + rand.nextInt(16);

            BlockPos pos = new BlockPos(posX, posY, posZ);

            new WorldGenMinable(
                    ore,
                    veinSize,
                    state -> state.getBlock() == Blocks.STONE
            ).generate(world, rand, pos);
        }
    }

}
