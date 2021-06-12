package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootInit
{
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MagicalJewelry.MOD_ID);

	public static final RegistryObject<JewelModifier.Serializer> JEWEL_MODIFIER = GLM.register("jewel_modifier", JewelModifier.Serializer::new);

	public static void init()
	{
		LootFunctionManager.registerFunction(SetJewelNBTFunction.Serializer.INSTANCE);
		LootFunctionManager.registerFunction(SetJewelNBTBossFunction.Serializer.INSTANCE);
	}
}