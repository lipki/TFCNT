package com.bioxx.tfc.WorldGen.Generators.Trees;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.WorldGen.DataLayer;

public class WorldGenCustomShortTrees extends WorldGenerator
{
	private int treeId;

	public WorldGenCustomShortTrees(boolean flag, int id)
	{
		super(flag);
		treeId=id;
	}
	@Override
	public boolean generate(World world, Random random, int xCoord, int yCoord, int zCoord)
	{
		int l = random.nextInt(3) + 4;
		boolean flag = true;
		if (yCoord < 1 || yCoord + l + 1 > world.getHeight())
			return false;

		for (int i1 = yCoord; i1 <= yCoord + 1 + l; i1++)
		{
			byte byte0 = 1;
			if (i1 == yCoord)
				byte0 = 0;
			if (i1 >= yCoord + 1 + l - 2)
				byte0 = 2;

			for (int i2 = xCoord - byte0; i2 <= xCoord + byte0 && flag; i2++)
			{
				for (int l2 = zCoord - byte0; l2 <= zCoord + byte0 && flag; l2++)
				{
					if (i1 >= 0 && i1 < world.getHeight())
					{
						Block j3 = world.getBlock(i2, i1, l2);
						if (j3 != Blocks.air && !j3.canBeReplacedByLeaves(world, i2, i1, l2))
							flag = false;
					}
					else
					{
						flag = false;
					}
				}
			}
		}

		if (!flag)
			return false;

		Block var3 = world.getBlock(xCoord, yCoord - 1, zCoord);
		if (treeId == 15)
		{
			int x = 0; // ???
		}

		if (!(TFC_Core.isSoil(var3))|| yCoord >= world.getHeight() - l - 1)
			return false;

		DataLayer rockLayer1 = TFC_Climate.getCacheManager(world).getRockLayerAt(xCoord, zCoord, 0);
		//set the block below the tree to dirt.
		//world.setBlockAndMetadata(xCoord, yCoord - 1, zCoord, TFC_Core.getTypeForDirt(rockLayer1.data2), TFC_Core.getSoilMetaFromStone(rockLayer1.block, rockLayer1.data2));
		for (int k1 = yCoord - 3 + l; k1 <= yCoord + l; k1++)
		{
			int j2 = k1 - (yCoord + l);
			int i3 = 1 - j2 / 2;
			for (int k3 = xCoord - i3; k3 <= xCoord + i3; k3++)
			{
				int l3 = k3 - xCoord;
				for (int i4 = zCoord - i3; i4 <= zCoord + i3; i4++)
				{
					int j4 = i4 - zCoord;
					if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && world.isAirBlock(k3, k1, i4))
						setBlockAndNotifyAdequately(world, k3, k1, i4, TFCBlocks.Leaves, treeId);
				}
			}
		}

		for (int l1 = 0; l1 < l; l1++)
		{
			Block b = world.getBlock(xCoord, yCoord + l1, zCoord);
			if (b == Blocks.air || b == TFCBlocks.Leaves || b == TFCBlocks.Leaves2 || b.canBeReplacedByLeaves(world, xCoord, yCoord + l1, zCoord))
				setBlockAndNotifyAdequately(world, xCoord, yCoord + l1, zCoord, TFCBlocks.LogNatural, treeId);
		}

		return true;
	}
}
