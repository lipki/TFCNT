package net.minecraft.src.TFC_Core;

import java.util.Random;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.StatList;
import net.minecraft.src.TFCItems;
import net.minecraft.src.World;
import net.minecraft.src.mod_TFC_Core;

public class BlockTerraOre2 extends BlockTerra
{

	public BlockTerraOre2(int i, Material material) {
		super(i,144, material);
	}

	public void addCreativeItems(java.util.ArrayList list)
    {
        for(int i = 0; i <16; i++) {
            list.add(new ItemStack(this,1,i));
        }
    }

    @Override
    public int damageDropped(int j) 
    {
        return j;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j) 
    {
        return blockIndexInTexture + j;
    }

	public int getRenderType()
	{
		return mod_TFC_Core.oreRenderId;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{
		entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
		entityplayer.addExhaustion(0.025F);
		Random random = new Random();

		ItemStack itemstack = new ItemStack(TFCItems.OreChunk, 1 ,damageDropped(l+16));

		if(l == 5)
		{
			//Drop diamonds
			itemstack = KimberliteGemSpawn();
		}
		else if(l == 13)
		{
			itemstack = new ItemStack(TFCItems.terraSaltpeterPowder, 1 + random.nextInt(3));
		}                      
		if (itemstack != null)
		{
			dropBlockAsItem_do(world, i, j, k, itemstack);
		}

	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return TFCItems.OreChunk.shiftedIndex;
	}

	public ItemStack KimberliteGemSpawn()
	{
		Random random = new Random();
		if(random.nextInt(25) == 0)
		{
			return new ItemStack(TFCItems.terraGemDiamond,1,0);
		}
		if(random.nextInt(50) == 0)
		{
			return new ItemStack(TFCItems.terraGemDiamond,1,1);
		}
		if(random.nextInt(75) == 0)
		{
			return new ItemStack(TFCItems.terraGemDiamond,1,2);
		}
		if(random.nextInt(150) == 0)
		{
			return new ItemStack(TFCItems.terraGemDiamond,1,3);
		}
		if(random.nextInt(300) == 0)
		{
			return new ItemStack(TFCItems.terraGemDiamond,1,4);
		} else {
			return null;
		}
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) 
	{
		int metadata = world.getBlockMetadata(i, j, k);

		//Minecraft mc = ModLoader.getMinecraftInstance();

		//mc.ingameGUI.addChatMessage("Meta="+(new StringBuilder()).append(getBlockName()).append(":").append(metadata).append(":").append(blockID).toString());  
	}
	
	@Override
    public String getTextureFile()
    {
        return "/bioxx/terraRock.png";
    }
	
	public static String[] blockNames = { "Kaolinite", "Gypsum", "Satinspar", "Selenite", "Graphite", "Kimberlite", 
        "Petrified Wood", "Sulfur", "Jet", "Microcline", "Pitchblende", "Cinnabar", "Cryolite", "Saltpeter", "Serpentine", "Sylvite"};
    
    public static String getItemNameDamage(int d) 
    {
        String s = blockNames[d];
        return s;
    }
}
