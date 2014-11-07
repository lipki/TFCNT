package com.bioxx.tfc.Render.TESR;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Render.Models.ModelIngotPile;
import com.bioxx.tfc.TileEntities.TEIngotPile;

public class TESRIngotPile extends TESRBase
{
	/** The normal small chest model. */
	private final ModelIngotPile ingotModel;
	private static String[] metalTypes =  new String[]{"Bismuth", "Bismuth Bronze", "Black Bronze", "Black Steel", "Blue Steel", "Brass", 
		"Bronze", "Copper", "Gold", "Wrought Iron", "Lead", "Nickel", "Pig Iron", "Platinum", "Red Steel", "Rose Gold", "Silver", "Steel",
		"Sterling Silver", "Tin", "Zinc", "Unknown" };
	
	public TESRIngotPile( boolean ddouble ) {
		ingotModel = new ModelIngotPile( ddouble );
	}

	/**
	 * Renders the TileEntity for the chest at a position.
	 */
	public void renderTileEntityIngotPileAt(TEIngotPile par1TileEntityPile, double d, double d1, double d2, float f)
	{
		int var9;

		if (par1TileEntityPile.getWorldObj() == null)
		{
			var9 = 0;
		}
		else
		{
			Block var10 = par1TileEntityPile.getBlockType();
			var9 = par1TileEntityPile.getBlockMetadata();

			if (par1TileEntityPile.getStackInSlot(0)!=null)
			{
				int i = ((com.bioxx.tfc.Blocks.BlockIngotPile)var10).getStack(par1TileEntityPile.getWorldObj(),par1TileEntityPile);
				TFC_Core.bindTexture(new ResourceLocation(Reference.ModID, "textures/blocks/metal/"+par1TileEntityPile.type+".png")); //texture
				GL11.glPushMatrix(); //start
				GL11.glTranslatef((float)d + 0.0F, (float)d1 + 0F, (float)d2 + 0.0F); //size

				ingotModel.renderIngots(i);
				GL11.glPopMatrix(); //end
			}
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.renderTileEntityIngotPileAt((TEIngotPile)par1TileEntity, par2, par4, par6, par8);
	}
}
