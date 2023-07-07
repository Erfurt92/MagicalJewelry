package com.erfurt.magicaljewelry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MagicalJewelry.MOD_ID)
public class MagicalJewelry
{
	public static final String MOD_ID = "magicaljewelry";
	public static final String MOD_NAME = "Magical Jewelry";
	
	public static MagicalJewelry instance;
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	public MagicalJewelry()
	{
		instance = this;

		DistExecutor.safeRunForDist(
				() -> SideProxy.Client::new,
				() -> SideProxy.Server::new
		);
	}

	public static ResourceLocation getId(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}