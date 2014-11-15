package com.bioxx.tfc.Blocks;

import java.util.BitSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bioxx.tfc.TFCBlocks;
import com.bioxx.tfc.Core.Player.PlayerInfo;
import com.bioxx.tfc.Core.Player.PlayerManagerTFC;
import com.bioxx.tfc.Items.Tools.ItemChisel;
import com.bioxx.tfc.Items.Tools.ItemHammer;
import com.bioxx.tfc.TileEntities.TEDetailed;
import com.bioxx.tfc.TileEntities.TEPartial;
import com.bioxx.tfc.api.TFCOptions;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDetailed extends BlockPartial
{
	public static int lockX = 0;
	public static int lockY = 0;
	public static int lockZ = 0;

	public BlockDetailed()
	{
		super(Material.rock);
	}

	@Override
	public int getRenderType()
	{
		return TFCBlocks.detailedRenderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		return true;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess bAccess, int i, int j, int k)
	{
		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegisterer)
	{
	}

	@Override
	public IIcon getIcon(IBlockAccess bAccess, int x, int y, int z, int side)
	{
		TEDetailed te = ((TEDetailed)bAccess.getTileEntity(x, y, z));
		return te.getBlockType().getIcon(side, te.MetaID);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TEDetailed();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		if (!TFCOptions.enableDetailedBlockSolidSide)
			return false;
		if (side == ForgeDirection.UNKNOWN)
			return false;

		int transpCount = TFCOptions.maxCountOfTranspSubBlocksOnSide;
		if (transpCount < 0 || transpCount >= 64)
			return false;

		TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);

		int start_x = (side == ForgeDirection.EAST ? 7 : 0);
		int end_x = (side == ForgeDirection.WEST ? 1 : 8);

		int start_y = (side == ForgeDirection.UP ? 7 : 0);
		int end_y = (side == ForgeDirection.DOWN ? 1 : 8);

		int start_z = (side == ForgeDirection.SOUTH ? 7 : 0);
		int end_z = (side == ForgeDirection.NORTH ? 1 : 8);

		for (int sub_x = start_x; sub_x < end_x; ++sub_x)
			for (int sub_y = start_y; sub_y < end_y; ++sub_y)
				for (int sub_z = start_z; sub_z < end_z; ++sub_z)
					if (!te.getBlockExists(sub_x, sub_y, sub_z) && --transpCount < 0)
						return false;

		return true;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess bAccess, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int side, float hitX, float hitY, float hitZ) 
	{
		boolean hasHammer = false;
		PlayerInfo pi = PlayerManagerTFC.getInstance().getPlayerInfoFromPlayer(entityplayer);
		for(int i = 0; i < 9;i++)
		{
			if(entityplayer.inventory.mainInventory[i] != null && entityplayer.inventory.mainInventory[i].getItem() instanceof ItemHammer)
				hasHammer = true;
		}

		if(entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().getItem() instanceof ItemChisel && hasHammer && world.isRemote && pi.lockMatches(x, y, z))
		{
			TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);
			lockX = x; lockY = y; lockZ = z;
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setByte("packetType", TEDetailed.Packet_Activate);
			nbt.setInteger("xSelected", xSelected);
			nbt.setInteger("ySelected", ySelected);
			nbt.setInteger("zSelected", zSelected);
			te.createDataNBT(nbt);
			te.broadcastPacketInRange(te.createDataPacket(nbt));
		}
		return false;
	}

	public boolean onBlockActivatedServer(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) 
	{
		int mode = getChiselMode( player );
		
		TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);

		int hasChisel = -1;
		int hasHammer = -1;

		for(int i = 0; i < 9;i++)
		{
			if(player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() instanceof ItemHammer)
				hasHammer = i;
			if(player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].getItem() instanceof ItemChisel)
				hasChisel = i;
		}

		if(mode == 1)
		{
			BitSet data = new BitSet();
			BlockStair.EmptyStairInt(xSelected, ySelected, zSelected, data, true);
			
			int index = -10;
			
			for(int subX = 0; subX < 8; subX++)
			for(int subZ = 0; subZ < 8; subZ++)
			for(int subY = 0; subY < 8; subY++)
			{
				index = (subX * 8 + subZ) * 8 + subY;
				if(data.get(index))
					deleteBox(world, x, y, z, player, te, index, hasChisel, hasHammer);
			}
			
			data = null;
			System.gc();
			
			return true;
		}
		else if(mode == 3 && xSelected != -10)
		{
			int index = (xSelected * 8 + zSelected) * 8 + ySelected;
			if(index >= 0)
				deleteBox(world, x, y, z, player, te, index, hasChisel, hasHammer);
			
			return true;
		}
		return false;
	}

	public void deleteBox(World world, int x, int y, int z, EntityPlayer player, TEDetailed te, int index, int hasChisel, int hasHammer)
	{
		te.data.clear(index);
		te.clearQuad(xSelected, ySelected, zSelected);
		if(te.isBlockEmpty())
		{
			world.setBlockToAir(x, y, z);
		}
		if(player.inventory.mainInventory[hasChisel] != null)
			player.inventory.mainInventory[hasChisel].damageItem(1, player);

		if(player.inventory.mainInventory[hasHammer] != null)
			player.inventory.mainInventory[hasHammer].damageItem(1, player);
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("packetType", TEDetailed.Packet_Update);
		nbt.setInteger("index", index);
		te.createDataNBT(nbt);
		te.broadcastPacketInRange(te.createDataPacket(nbt));
	}

	@Override
	public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB aabb, List list, Entity entity)
	{
		TEDetailed te = (TEDetailed) world.getTileEntity(i, j, k);
		float div = 1f / 8;

		for(int subX = 0; subX < 8; subX++)
		{
			for(int subZ = 0; subZ < 8; subZ++)
			{
				for(int subY = 0; subY < 8; subY++)
				{
					if (te.data.get((subX * 8 + subZ)*8 + subY))
					{
						float minX = subX * div;
						float maxX = minX + div;
						float minY = subY * div;
						float maxY = minY + div;
						float minZ = subZ * div;
						float maxZ = minZ + div;

						this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
						super.addCollisionBoxesToList(world, i, j, k, aabb, list, entity);
					}
				}
			}
		}
		setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
	}
	
	public static int side = -1;

	@Override
	public BitSet getData( World world, int x, int y, int z ) {
		
		BitSet data = new BitSet();
		TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);
		if (te == null) return data;
		
		if( chiselmode == 1 ) {
			int subX = -1,subY = -1,subZ = -1;
			search: {
				for(subX = 0; subX < 4; subX++) for(subZ = 0; subZ < 4; subZ++) for(subY = 0; subY < 4; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 4 || subY != 4 || subZ != 4) data.set((0 * 2 + 0)*2 + 0);

			search: {
				for(subX = 4; subX < 8; subX++) for(subZ = 0; subZ < 4; subZ++) for(subY = 0; subY < 4; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 8 || subY != 4 || subZ != 4) data.set((1 * 2 + 0)*2 + 0);
			
			search: {
				for(subX = 4; subX < 8; subX++) for(subZ = 4; subZ < 8; subZ++) for(subY = 0; subY < 4; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 8 || subY != 4 || subZ != 8) data.set((1 * 2 + 1)*2 + 0);
			
			search: {
				for(subX = 0; subX < 4; subX++) for(subZ = 4; subZ < 8; subZ++) for(subY = 0; subY < 4; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 4 || subY != 4 || subZ != 8) data.set((0 * 2 + 1)*2 + 0);
			
			search: {
				for(subX = 0; subX < 4; subX++) for(subZ = 0; subZ < 4; subZ++) for(subY = 4; subY < 8; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 4 || subY != 8 || subZ != 4) data.set((0 * 2 + 0)*2 + 1);
			
			search: {
				for(subX = 4; subX < 8; subX++) for(subZ = 0; subZ < 4; subZ++) for(subY = 4; subY < 8; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 8 || subY != 8 || subZ != 4) data.set((1 * 2 + 0)*2 + 1);
			
			search: {
				for(subX = 4; subX < 8; subX++) for(subZ = 4; subZ < 8; subZ++) for(subY = 4; subY < 8; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 8 || subY != 8 || subZ != 8) data.set((1 * 2 + 1)*2 + 1);
			
			search: {
				for(subX = 0; subX < 4; subX++) for(subZ = 4; subZ < 8; subZ++) for(subY = 4; subY < 8; subY++)
					if(te.data.get((subX * 8 + subZ)*8 + subY)) break search;
			}
			if(subX != 4 || subY != 8 || subZ != 8) data.set((0 * 2 + 1)*2 + 1);
		
		} else if ( chiselmode == 3 ) {
			data = te.data;
		}
		
		return data;
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);
		if(te.TypeID >= 0)
			return Blocks.fire.getFlammability(Block.getBlockById(te.TypeID));
		else return 0;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		TEDetailed te = (TEDetailed) world.getTileEntity(x, y, z);
		if(te.TypeID >= 0)
			return Blocks.fire.getEncouragement(Block.getBlockById(te.TypeID));
		else return 0;
	}

	public static BitSet EmptyDetailedInt(int xSelected, int ySelected, int zSelected, BitSet data) {
		data.set((xSelected * 8 + zSelected)*8 + ySelected, false);
		return data;
	}
	
	public static void ListToDetailled(World world, int x, int y, int z, BitSet data)
	{

		TEPartial tep = (TEPartial)world.getTileEntity(x, y, z);
		world.setBlock(x, y, z, TFCBlocks.Detailed);
		
		TEDetailed te;
		te = (TEDetailed)world.getTileEntity(x, y, z);
		te.TypeID = tep.TypeID;
		te.MetaID = tep.MetaID;
		
		for(int subX = 0; subX < 8; subX++)
		for(int subZ = 0; subZ < 8; subZ++)
		for(int subY = 0; subY < 8; subY++)
			if(data.get((subX * 8 + subZ)*8 + subY))
			{
				te.setBlock(subX, subY, subZ);
				te.setQuad(subX, subY, subZ);
			}
		
		world.notifyBlocksOfNeighborChange(x, y, z, world.getBlock(x, y, z));
		
		return;
	}
}
