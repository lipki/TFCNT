package com.bioxx.tfc.Render.Blocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Blocks.BlockBucketPile;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBucketPile implements ISimpleBlockRenderingHandler {
	
	static float p = 1f/16f;
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		int meta = world.getBlockMetadata(x, y, z);
		
		renderer.overrideBlockTexture = block.getIcon(0, 0);
		renderer.renderAllFaces = true;
		
		float start = 0;
		
		if( ((BlockBucketPile) block).getMaxMeta(world, x, y, z) < 15 ) start = 8*p;
		
		tube( block, renderer, x, y, z, 8*p, 8*p, 4*p, 1*p, 0, (meta+1)*p+start );

		renderer.setRenderBounds(4*p, meta*p-8*p+start, 4*p, 12*p, meta*p+1*p-8*p+start, 12*p);
		renderer.renderStandardBlock(block, x, y, z);
		
		for( int mi = 0 ; mi <= meta ; mi ++ )
			tube( block, renderer, x, y, z, 8*p, 8*p, 5*p, 1*p, start+mi*p+.2F*p, .8F*p );
		
		renderer.renderAllFaces = false;
		renderer.clearOverrideBlockTexture();
		
		return true;
	}
	
	public void tube( Block block, RenderBlocks renderer, int x, int y, int z, float cx, float cy, float ri, float t, float pb, float h ) {
		
		renderer.setRenderBounds(cx-ri-t, pb, cy-ri, cx-ri, pb+h, cy+ri);
		renderer.renderStandardBlock(TFCBlocks.Planks, x, y, z);
		
		renderer.setRenderBounds(cx+ri, pb, cy-ri, cx+ri+t, pb+h, cy+ri);
		renderer.renderStandardBlock(TFCBlocks.Planks, x, y, z);
		
		renderer.setRenderBounds(cx-ri-t, pb, cy-ri-t, cx+ri+t, pb+h, cy-ri);
		renderer.renderStandardBlock(TFCBlocks.Planks, x, y, z);
		
		renderer.setRenderBounds(cx-ri-t, pb, cy+ri, cx+ri+t, pb+h, cy+ri+t);
		renderer.renderStandardBlock(TFCBlocks.Planks, x, y, z);
		
	}

	@Override
	public int getRenderId() {
		return 0;
	}

	@Override
	public void renderInventoryBlock(Block arg0, int arg1, int arg2, RenderBlocks arg3) {
	}

	@Override
	public boolean shouldRender3DInInventory(int arg0) {
		return false;
	}
}
