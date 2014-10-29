package com.bioxx.tfc.Render.Blocks;

import com.bioxx.tfc.TileEntities.TELogPile;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderLogPile implements ISimpleBlockRenderingHandler {
	
	public static float p = 1f/16f;
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		
		block.setBlockBoundsBasedOnState(world, x, y, z);
		
		TELogPile te = (TELogPile) world.getTileEntity(x, y, z);
		int nbl = te.getNumberOfLogs()-1;
		
		int meta = world.getBlockMetadata(x, y, z);
		
    	float dx = 1F/4;
    	float dy = (1-p)/4;
		
		int py = 0;
		int px = 0;
		for( int mi = 0 ; mi <= nbl ; mi ++ ) {
			if( px == 3 ) py += 1;
			px = mi % 4;
			
			float rand = (Math.abs(px + py + mi + x + y + z) % 3)*p/2; // pseudo random
			
			if( meta == 0 || meta == 2 ) {
				renderer.setRenderBounds(px*dx, p+py*dy, rand, (px+1)*dx, p+(py+1)*dy, 1-1.5*p+rand);
				renderLog(world, block, x, y, z, renderer, meta) ;
			} else {
				renderer.setRenderBounds(rand, p+py*dy, px*dx, 1-1.5*p+rand, p+(py+1)*dy, (px+1)*dx);
				renderLog(world, block, x, y, z, renderer, meta) ;
			}
		}

		if( meta == 0 || meta == 2 ) {
			renderer.setRenderBounds(0, 0, 3*p, 1, 1*p, 4*p);
			renderCleat(world, block, x, y, z, renderer);
			renderer.setRenderBounds(0, 0, 12*p, 1, 1*p, 13*p);
			renderCleat(world, block, x, y, z, renderer);
		} else {
			renderer.setRenderBounds(3*p, 0, 0, 4*p, 1*p, 1);
			renderCleat(world, block, x, y, z, renderer);
			renderer.setRenderBounds(12*p, 0, 0, 13*p, 1*p, 1);
			renderCleat(world, block, x, y, z, renderer);
		}
		
		return true;
	}

    public boolean renderLog(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer, int meta) {

        IIcon iicon;
        Tessellator tessellator = Tessellator.instance;
        color( world, block, x, y, z, renderer );

        //0
        tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(world, x, y - 1, z));
        tessellator.setColorOpaque_F(f10, f13, f16);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, iicon);

        //1
        tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(world, x, y + 1, z));
        tessellator.setColorOpaque_F(f7, f8, f9);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 1);
        if( meta == 1 || meta == 3 )
       	 	 renderFaceYPos2(block, (double)x, (double)y, (double)z, iicon, renderer);
		else renderFaceYPos(block, (double)x, (double)y, (double)z, iicon, renderer);

        //2
        tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(world, x, y, z - 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 2);
        if( meta == 0 || meta == 2 )
	         renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon, renderer);
		else renderFaceZNeg2(block, (double)x, (double)y, (double)z, iicon, renderer);

        //3
        tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(world, x, y, z + 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 3);
        if( meta == 0 || meta == 2 )
	         renderFaceZPos(block, (double)x, (double)y, (double)z, iicon, renderer);
		else renderFaceZPos2(block, (double)x, (double)y, (double)z, iicon, renderer);

        //4
        tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(world, x - 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 4);
        if( meta == 1 || meta == 3 )
        	 renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon, renderer);
		else renderFaceXNeg2(block, (double)x, (double)y, (double)z, iicon, renderer);

        //5
        tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(world, x + 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 5);
        if( meta == 1 || meta == 3 )
        	 renderFaceXPos(block, (double)x, (double)y, (double)z, iicon, renderer);
		else renderFaceXPos2(block, (double)x, (double)y, (double)z, iicon, renderer);

        return true;
    }

    public boolean renderCleat(IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer) {

        IIcon iicon;
        Tessellator tessellator = Tessellator.instance;
        color( world, block, x, y, z, renderer );

        //0
        tessellator.setBrightness(renderer.renderMinY > 0.0D ? l : block.getMixedBrightnessForBlock(world, x, y - 1, z));
        tessellator.setColorOpaque_F(f10, f13, f16);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, iicon);

        //1
        tessellator.setBrightness(renderer.renderMaxY < 1.0D ? l : block.getMixedBrightnessForBlock(world, x, y + 1, z));
        tessellator.setColorOpaque_F(f7, f8, f9);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, iicon);

        //2
        tessellator.setBrightness(renderer.renderMinZ > 0.0D ? l : block.getMixedBrightnessForBlock(world, x, y, z - 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

        //3
        tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? l : block.getMixedBrightnessForBlock(world, x, y, z + 1));
        tessellator.setColorOpaque_F(f11, f14, f17);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);

        //4
        tessellator.setBrightness(renderer.renderMinX > 0.0D ? l : block.getMixedBrightnessForBlock(world, x - 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

        //5
        tessellator.setBrightness(renderer.renderMaxX < 1.0D ? l : block.getMixedBrightnessForBlock(world, x + 1, y, z));
        tessellator.setColorOpaque_F(f12, f15, f18);
        iicon = renderer.getBlockIcon(block, world, x, y, z, 0);
        renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

        return true;
    }
	
	public float f7;
	public float f8;
	public float f9;
	public float f10;
	public float f11;
	public float f12;
	public float f13;
	public float f14;
	public float f15;
	public float f16;
	public float f17;
	public float f18;
	public int l;
	
	public void color( IBlockAccess world, Block block, int x, int y, int z, RenderBlocks renderer ) {

        int cm = block.colorMultiplier(world, x, y, z);
        float r = (float)(cm >> 16 & 255) / 255.0F;
        float g = (float)(cm >> 8 & 255) / 255.0F;
        float b = (float)(cm & 255) / 255.0F;
        
        renderer.enableAO = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        f7 = f4 * r;
        f8 = f4 * g;
        f9 = f4 * b;
        f10 = f3 * r;
        f11 = f5 * r;
        f12 = f6 * r;
        f13 = f3 * g;
        f14 = f5 * g;
        f15 = f6 * g;
        f16 = f3 * b;
        f17 = f5 * b;
        f18 = f6 * b;

        l = block.getMixedBrightnessForBlock(world, x, y, z);
        
	}
    
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(renderer.renderMinZ * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(renderer.renderMaxZ * 16.0D);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d12, d13, d15, d3, d6);
        tessellator.addVertexWithUV(d12, d13, d14, d3, d5);
        tessellator.addVertexWithUV(d11, d13, d14, d4, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
    }
    
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(0);
        double d4 = (double)iicon.getInterpolatedU(16);

        double d5 = (double)iicon.getInterpolatedV(0);
        double d6 = (double)iicon.getInterpolatedV(16);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMinY;
        double d14 = y + renderer.renderMaxY;
        double d15 = z + renderer.renderMinZ;
        
        tessellator.addVertexWithUV(d11, d14, d15, d4, d5);
        tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
        tessellator.addVertexWithUV(d12, d13, d15, d3, d6);
        tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
    }
    
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(0);
        double d4 = (double)iicon.getInterpolatedU(16);

        double d5 = (double)iicon.getInterpolatedV(0);
        double d6 = (double)iicon.getInterpolatedV(16);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMinY;
        double d14 = y + renderer.renderMaxY;
        double d15 = z + renderer.renderMaxZ;
        
        tessellator.addVertexWithUV(d12, d14, d15, d4, d5);
        tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d3, d6);
        tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
    }

    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(0);
        double d4 = (double)iicon.getInterpolatedU(16);
        
        double d5 = (double)iicon.getInterpolatedV(0);
        double d6 = (double)iicon.getInterpolatedV(16);

        double d11 = x + renderer.renderMinX;
        double d12 = y + renderer.renderMinY;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d11, d13, d15, d4, d5);
        tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
        tessellator.addVertexWithUV(d11, d12, d14, d3, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
    }

    public void renderFaceXPos(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(0);
        double d4 = (double)iicon.getInterpolatedU(16);
        
        double d5 = (double)iicon.getInterpolatedV(0);
        double d6 = (double)iicon.getInterpolatedV(16);

        double d11 = x + renderer.renderMaxX;
        double d12 = y + renderer.renderMinY;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d11, d12, d15, d3, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d13, d14, d4, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
    }
    
    
    
    

    
    public void renderFaceYPos2(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(renderer.renderMinZ * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(renderer.renderMaxZ * 16.0D);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d11, d13, d14, d3, d6);
        tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
        tessellator.addVertexWithUV(d12, d13, d15, d4, d5);
        tessellator.addVertexWithUV(d12, d13, d14, d4, d6);
    }
    
    public void renderFaceZNeg2(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;

        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(renderer.renderMinZ * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(renderer.renderMaxZ * 16.0D);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMinY;
        double d14 = y + renderer.renderMaxY;
        double d15 = z + renderer.renderMinZ;
        
        tessellator.addVertexWithUV(d12, d13, d15, d4, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
        tessellator.addVertexWithUV(d11, d14, d15, d3, d6);
        tessellator.addVertexWithUV(d12, d14, d15, d4, d6);
    }
    
    public void renderFaceZPos2(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer) {
    	
        Tessellator tessellator = Tessellator.instance;
        
        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);

        double d11 = x + renderer.renderMinX;
        double d12 = x + renderer.renderMaxX;
        double d13 = y + renderer.renderMinY;
        double d14 = y + renderer.renderMaxY;
        double d15 = z + renderer.renderMaxZ;
        
        tessellator.addVertexWithUV(d11, d13, d15, d4, d5);
        tessellator.addVertexWithUV(d12, d13, d15, d3, d5);
        tessellator.addVertexWithUV(d12, d14, d15, d3, d6);
        tessellator.addVertexWithUV(d11, d14, d15, d4, d6);
    }

    public void renderFaceXNeg2(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        
        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinZ * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxZ * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);

        double d11 = x + renderer.renderMinX;
        double d12 = y + renderer.renderMinY;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d11, d12, d14, d4, d5);
        tessellator.addVertexWithUV(d11, d12, d15, d3, d5);
        tessellator.addVertexWithUV(d11, d13, d15, d3, d6);
        tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
    }

    public void renderFaceXPos2(Block block, double x, double y, double z, IIcon iicon, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        
        double d3 = (double)iicon.getInterpolatedU(renderer.renderMinZ * 16.0D);
        double d4 = (double)iicon.getInterpolatedU(renderer.renderMaxZ * 16.0D);
        double d5 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMaxY * 16.0D);
        double d6 = (double)iicon.getInterpolatedV(16.0D - renderer.renderMinY * 16.0D);

        double d11 = x + renderer.renderMaxX;
        double d12 = y + renderer.renderMinY;
        double d13 = y + renderer.renderMaxY;
        double d14 = z + renderer.renderMinZ;
        double d15 = z + renderer.renderMaxZ;

        tessellator.addVertexWithUV(d11, d13, d14, d3, d6);
        tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d4, d5);
        tessellator.addVertexWithUV(d11, d12, d14, d3, d5);
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
