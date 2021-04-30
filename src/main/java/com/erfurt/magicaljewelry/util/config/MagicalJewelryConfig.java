package com.erfurt.magicaljewelry.util.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import static com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder.ClientConfig;
import static com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder.CommonConfig;

public class MagicalJewelryConfig
{
	public static final CommonConfig COMMON_BUILDER;

	public static final ForgeConfigSpec COMMON_CONFIG;

	public static final ClientConfig CLIENT_BUILDER;

	public static final ForgeConfigSpec CLIENT_CONFIG;

	static
	{
		final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON_CONFIG = commonSpecPair.getRight();
		COMMON_BUILDER = commonSpecPair.getLeft();

		final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT_CONFIG = clientSpecPair.getRight();
		CLIENT_BUILDER = clientSpecPair.getLeft();
	}
}