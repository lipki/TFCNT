package com.bioxx.tfc.Blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.TFCItems;

public class BlockBucketPile extends BlockTerra
{
	
	public BlockBucketPile()
	{
		super(Material.wood);
		//this.item = TFCItems.WoodenBucketEmpty;
		//this.itemeta = 0;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegisterer)
	{
		this.blockIcon = iconRegisterer.registerIcon( Reference.ModID + ":wood/Aspen Plank" );
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.bucketPileRenderId;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		float p = 1/16F;
		float start = 0;
		if( getMaxMeta(world, x, y, z) < 15 ) start = 8*p;
		return AxisAlignedBB.getBoundingBox(x+2*p, y, z+2*p, x + 1-2*p, y + p*(meta+1)+start, z + 1-2*p);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess bAccess, int x, int y, int z)
	{
		int meta = bAccess.getBlockMetadata(x, y, z);
		float p = 1/16F;
		float start = 0;
		if( getMaxMeta(bAccess, x, y, z) < 15 ) start = 8*p;
		setBlockBounds(2*p, 0, 2*p, 1-2*p, p*(meta+1)+start, 1-2*p);
	}
	
	public int getMaxMeta(IBlockAccess world, int x, int y, int z)
	{
		if( world.getBlock(x, y-1, z) == TFCBlocks.BucketPile )
			return 15;
		return 7;
	}

	public void combine(World world, int x, int y, int z)
	{
		
		int meta = world.getBlockMetadata(x, y, z);
		int bottomMeta = world.getBlockMetadata(x, y - 1, z);
		int maxBottomMeta = getMaxMeta(world, x, y - 1, z);

		if(bottomMeta < maxBottomMeta) {
			
			bottomMeta = bottomMeta + meta +1;
			int m2 = 0;
			if(bottomMeta > maxBottomMeta) {
				m2 = bottomMeta - maxBottomMeta;
				bottomMeta = maxBottomMeta;
			}

			world.setBlock(x, y - 1, z, this, bottomMeta, 0x2);

			if(m2 > 0) {
				world.setBlock(x, y, z, this, m2, 0x2);
				world.notifyBlockOfNeighborChange(x, y + 1, z, this);
			} else world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		
		if(!world.isRemote) {
			if(world.isAirBlock(x, y - 1, z)) {
				int meta = world.getBlockMetadata(x, y, z);
				world.setBlock(x, y - 1, z, this, meta, 0x2);
				world.setBlockToAir(x, y, z);
			} else {
				if(world.getBlock(x, y - 1, z) == this)
					combine(world, x, y, z);

				if(world.getBlock(x, y + 1, z) == this)
					combine(world, x, y + 1, z);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)
	{
		
		super.onBlockActivated(world, x, y, z, entityplayer, side, hitX, hitY, hitZ);
		
		if( !world.isRemote ) {
			
			ItemStack ei = entityplayer.getCurrentEquippedItem();
			if( ei != null && ei.getItem() == TFCItems.WoodenBucketEmpty ) {
				
				int[][] map = {{0,-1,0},{0,1,0},{0,0,-1},{0,0,1},{-1,0,0},{1,0,0}};
				int[] face = map[side];
			
				if(world.getBlock(x, y, z) == this) {

					int meta = world.getBlockMetadata(x, y, z);
					if(meta < getMaxMeta(world, x, y, z)) {
						world.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
						if ( !entityplayer.capabilities.isCreativeMode ) ei.stackSize--;
					} else if(world.isAirBlock(x + face[0], y + face[1], z + face[2])) {
						world.setBlock(x + face[0], y + face[1], z + face[2], this, 0, 0x2);
						if ( !entityplayer.capabilities.isCreativeMode ) ei.stackSize--;
					}
				} else if(world.getBlock(x + face[0], y + face[1], z + face[2]) == this) {
					
					int meta = world.getBlockMetadata(x + face[0], y + face[1], z + face[2]);
					if(meta < getMaxMeta(world, x, y, z)) {
						world.setBlockMetadataWithNotify(x + face[0], y + face[1], z + face[2], meta + 1, 3);
						if ( !entityplayer.capabilities.isCreativeMode ) ei.stackSize--;
					}
				}
			} else if( ei == null ) {
				
				dropBlockAsItem(world, x, y, z, new ItemStack(TFCItems.WoodenBucketEmpty, 1, 0));

				int meta = world.getBlockMetadata(x, y, z);
				world.setBlockMetadataWithNotify(x, y, z, meta - 1, 3);
				if( meta == 0 ) world.setBlockToAir(x, y, z);
				
			}
		}
		
		return false;
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion ex)
	{
		
		if(!world.isRemote) {
			int amount = world.getBlockMetadata(x, y, z);
			if(amount > 0) {
				Random rand = new Random();
				// Between 50% and 100% of the amount
				amount = rand.nextInt(amount + 1) + (amount / 2);
				dropBlockAsItem(world, x, y, z, new ItemStack(TFCItems.WoodenBucketEmpty, 1, 0));
			}
		}

		super.onBlockExploded(world, x, y, z, ex);
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex) {}

	
}
