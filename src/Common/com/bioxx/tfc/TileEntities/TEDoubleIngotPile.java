package com.bioxx.tfc.TileEntities;

import net.minecraft.inventory.IInventory;

public class TEDoubleIngotPile extends TEIngotPile implements IInventory
{

	@Override
	public int getInventoryStackLimit()
	{
		return 32;
	}

	@Override
	public String getInventoryName()
	{
		return "Double Ingot Pile";
	}

}
