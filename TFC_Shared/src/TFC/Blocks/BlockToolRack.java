package TFC.Blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import TFC.TFCBlocks;
import TFC.TFCItems;
import TFC.Items.ItemProPick;
import TFC.Items.ItemWeapon;
import TFC.TileEntities.TileEntityToolRack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockToolRack extends BlockTerraContainer
{
	public BlockToolRack(int par1)
	{
		super(par1, Material.wood);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return true;
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public Icon getBlockTexture(int woodType)
	{
		return TFCBlocks.WoodSupportH.getBlockTextureFromSideAndMetadata(0, woodType);
	}
	@Override
	public int getRenderType()
	{
		return TFCBlocks.toolRackRenderId;
	}
	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
		{
			TileEntityToolRack te = (TileEntityToolRack) world.getBlockTileEntity(i, j, k);
			int dir = world.getBlockMetadata(i, j, k);
			if(te != null)
			{
				if(dir == 0)
				{
					if(hitX < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0, 0);
					}
					else if(hitX > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1, 0);
					}
					else if(hitX < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2, 0);
					}
					else if(hitX > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3, 0);
					}
				}
				else if(dir == 1)
				{
					if(hitZ < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0, 1);
					}
					else if(hitZ > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1, 1);
					}
					else if(hitZ < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2, 1);
					}
					else if(hitZ > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3, 1);
					}
				}
				else if(dir == 2)
				{
					if(hitX < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0, 2);
					}
					else if(hitX > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1, 2);
					}
					else if(hitX < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2, 2);
					}
					else if(hitX > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3, 2);
					}
				}
				else if(dir == 3)
				{
					if(hitZ < 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 0, 3);
					}
					else if(hitZ > 0.5 && hitY > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 1, 3);
					}
					else if(hitZ < 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 2, 3);
					}
					else if(hitZ > 0.5)
					{
						handleArea(world, i, j, k, entityplayer, te, 3, 3);
					}
				}
				te.broadcastPacketInRange(te.createUpdatePacket());
				return true;
			}
		}
		return false;
	}

	private void handleArea(World world, int i, int j, int k,EntityPlayer entityplayer, TileEntityToolRack te, int slot, int dir) 
	{
		boolean hasToolInHand = entityplayer.getCurrentEquippedItem() != null && 
				(entityplayer.getCurrentEquippedItem().getItem() instanceof ItemTool || entityplayer.getCurrentEquippedItem().getItem() instanceof ItemWeapon  || 
						entityplayer.getCurrentEquippedItem().getItem() instanceof ItemHoe || entityplayer.getCurrentEquippedItem().getItem() instanceof ItemProPick);
		if(te.storage[slot] == null && hasToolInHand)
		{
			te.storage[slot] = entityplayer.getCurrentEquippedItem().copy();
			entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
		}
		else if(te.storage[slot] != null)
		{
			te.ejectItem(slot, dir);
			te.storage[slot] = null;
		}
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int meta)
	{
		if(!world.isRemote)
		{
			dropBlockAsItem_do(world, i, j, k, new ItemStack(TFCItems.Toolrack, 1, meta));
		}
	}
	
	@Override
	public int idDropped(int i, Random random, int j)
	{
		return TFCItems.LooseRock.itemID;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		return new TileEntityToolRack();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, int i, int j, int k) 
	{
		int dir = access.getBlockMetadata(i, j, k);

		if(dir == 0)
		{
			this.setBlockBounds(0.0F, 0F, 0.85F, 1F, 1F, 1F);
		}
		else if(dir == 1)
		{
			this.setBlockBounds(0.0F, 0F, 0.0F, 0.15F, 1F, 1F);
		}
		else if(dir == 2)
		{
			this.setBlockBounds(0.0F, 0F, 0.00F, 1F, 1F, 0.15F);
		}
		else if(dir == 3)
		{
			this.setBlockBounds(0.85F, 0F, 0.0F, 1F, 1F, 1F);
		}
	}

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	int dir = world.getBlockMetadata(i, j, k);

		if(dir == 0)
		{
			return AxisAlignedBB.getBoundingBox(i+0.0F, j+0F, k+0.85F, i+1F, j+1F, k+1F);
		}
		else if(dir == 1)
		{
			return AxisAlignedBB.getBoundingBox(i+0.0F, j+0F, k+0.0F, i+0.15F, j+1F, k+1F);
		}
		else if(dir == 2)
		{
			return AxisAlignedBB.getBoundingBox(i+0.0F, j+0F, k+0.00F, i+1F, j+1F, k+0.15F);
		}
		else if(dir == 3)
		{
			return AxisAlignedBB.getBoundingBox(i+0.85F, j+0F, k+0.0F, i+1F, j+1F, k+1F);
		}
		
        return AxisAlignedBB.getBoundingBox(i, j, k, i+1, j+1, k+1);
    }
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) 
	{
		int dir = world.getBlockMetadata(i, j, k);

		if(dir == 0)
		{
			if(!world.isBlockOpaqueCube(i, j, k+1))
			{
				this.breakBlock(world, i, j, k, blockID, dir);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 1)
		{
			if(!world.isBlockOpaqueCube(i-1, j, k))
			{
				this.breakBlock(world, i, j, k, blockID, dir);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 2)
		{
			if(!world.isBlockOpaqueCube(i, j, k-1))
			{
				this.breakBlock(world, i, j, k, blockID, dir);
				world.setBlock(i, j, k, 0);
			}
		}
		else if(dir == 3)
		{
			if(!world.isBlockOpaqueCube(i+1, j, k))
			{
				this.breakBlock(world, i, j, k, blockID, dir);
				world.setBlock(i, j, k, 0);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(int i = 0; i < 16; i++)
    		par3List.add(new ItemStack(par1, 1, i));
    }

	@Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		return TFCBlocks.WoodSupportH.getBlockTexture(par1IBlockAccess, par2, par3, par4, par5);
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
    	return TFCBlocks.WoodSupportH.getBlockTextureFromSideAndMetadata(par1, par2);
    }
    
    @Override
	public void registerIcons(IconRegister iconRegisterer)
    {
    	//Empty On Purpose
    }
}
