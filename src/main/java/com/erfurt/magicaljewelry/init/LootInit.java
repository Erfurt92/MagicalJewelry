package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.JewelModifier;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.WaterHostileEntityCondition;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class LootInit
{
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MagicalJewelry.MOD_ID);

	public static final RegistryObject<Codec<JewelModifier>> JEWEL_MODIFIER = GLM.register("jewel_modifier", JewelModifier.CODEC);

	public static final LootItemConditionType HOSTILE_ENTITY_CONDITION = new LootItemConditionType(HostileEntityCondition.SERIALIZER);
	public static final LootItemConditionType BOSS_ENTITY_CONDITION = new LootItemConditionType(BossEntityCondition.SERIALIZER);
	public static final LootItemConditionType WATER_ENTITY_CONDITION = new LootItemConditionType(WaterEntityCondition.SERIALIZER);
	public static final LootItemConditionType WATER_HOSTILE_ENTITY_CONDITION = new LootItemConditionType(WaterHostileEntityCondition.SERIALIZER);

	public static final LootItemFunctionType SET_JEWEL_NBT_FUNCTION = new LootItemFunctionType(SetJewelNBTFunction.SERIALIZER);
	public static final LootItemFunctionType SET_JEWEL_NBT_BOSS_FUNCTION = new LootItemFunctionType(SetJewelNBTBossFunction.SERIALIZER);

	public static void init(RegisterEvent event)
	{
		event.register(Registries.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_hostile_entity"), () -> HOSTILE_ENTITY_CONDITION);
		event.register(Registries.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_boss_entity"), () -> BOSS_ENTITY_CONDITION);
		event.register(Registries.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_water_entity"), () -> WATER_ENTITY_CONDITION);
		event.register(Registries.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_water_hostile_entity"), () -> WATER_HOSTILE_ENTITY_CONDITION);

		event.register(Registries.LOOT_FUNCTION_TYPE, MagicalJewelry.getId("set_jewel_nbt"), () -> SET_JEWEL_NBT_FUNCTION);
		event.register(Registries.LOOT_FUNCTION_TYPE, MagicalJewelry.getId("set_jewel_nbt_boss"), () -> SET_JEWEL_NBT_BOSS_FUNCTION);
	}
}