package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.loot.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.SetJewelNBTFunction;

import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class LootInit
{
	public static void init()
	{
		LootFunctionManager.registerFunction(SetJewelNBTFunction.Serializer.INSTANCE);
		LootFunctionManager.registerFunction(SetJewelNBTBossFunction.Serializer.INSTANCE);
	}
}