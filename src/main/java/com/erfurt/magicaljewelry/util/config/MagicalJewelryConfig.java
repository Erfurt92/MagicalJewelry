package com.erfurt.magicaljewelry.util.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;

public class MagicalJewelryConfig
{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	static
	{
		MagicalJewelryConfigBuilder.init(BUILDER);
		
		CONFIG = BUILDER.build();
	}
	
	public static void loadConfig(ForgeConfigSpec spec, Path path)
	{
		final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
		
		configData.load();
		
		spec.setConfig(configData);
	}
}