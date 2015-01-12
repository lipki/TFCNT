package com.bioxx.tfc.api.Crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BarrelLiquidToLiquidRecipe extends BarrelRecipe
{
	FluidStack inputfluid;
	public BarrelLiquidToLiquidRecipe(FluidStack fluidInBarrel, FluidStack inputfluid, FluidStack outputFluid)
	{
		super(null, fluidInBarrel, null, outputFluid);
		this.inputfluid = inputfluid;
	}

	@Override
	public Boolean matches(ItemStack item, FluidStack fluid)
	{
		FluidStack itemLiquid = FluidContainerRegistry.getFluidForFilledItem(item);
		if(recipeFluid != null && recipeFluid.isFluidEqual(fluid) && itemLiquid != null && itemLiquid.isFluidEqual(inputfluid))
		{
			//Make sure that when we combine the liquids that there is enough room in the barrel for the new liquid to fit
			if(10000-fluid.amount < itemLiquid.amount)
				return false;

			return true;
		}
		return false;
	}

	@Override
	public ItemStack getResult(ItemStack inIS, FluidStack inFS, int sealedTime)
	{
		if(inIS != null)
			return inIS.getItem().getContainerItem(inIS);
		return null;
	}

	@Override
	public FluidStack getResultFluid(ItemStack inIS, FluidStack inFS, int sealedTime)
	{
		if(recipeOutFluid != null)
		{
			FluidStack fs = recipeOutFluid.copy();
			FluidStack itemLiquid = FluidContainerRegistry.getFluidForFilledItem(inIS);
			if(!removesLiquid)
			{
				fs.amount = inFS.amount+itemLiquid.amount;
			}
			else
			{
				fs.amount = ( fs.amount * inFS.amount ) / recipeFluid.amount;
			}
			return fs;
		}
		return null;
	}

	public FluidStack getInputfluid()
	{
		return inputfluid;
	}
}
