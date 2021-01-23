package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.loot.conditions.BossEntityCondition;
import com.erfurt.magicaljewelry.loot.conditions.HostileEntityCondition;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTBossFunction;
import com.erfurt.magicaljewelry.loot.functions.SetJewelNBTFunction;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.registry.Registry;

public class LootInit
{
	public static final LootConditionType HOSTILE_ENTITY_CONDITION = new LootConditionType(HostileEntityCondition.SERIALIZER);
	public static final LootConditionType BOSS_ENTITY_CONDITION = new LootConditionType(BossEntityCondition.SERIALIZER);

	public static final LootFunctionType SET_JEWEL_NBT_FUNCTION = new LootFunctionType(SetJewelNBTFunction.SERIALIZER);
	public static final LootFunctionType SET_JEWEL_NBT_BOSS_FUNCTION = new LootFunctionType(SetJewelNBTBossFunction.SERIALIZER);

	public static void init()
	{
		Registry.register(Registry.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_hostile_entity"), HOSTILE_ENTITY_CONDITION);
		Registry.register(Registry.LOOT_CONDITION_TYPE, MagicalJewelry.getId("is_boss_entity"), BOSS_ENTITY_CONDITION);

		Registry.register(Registry.LOOT_FUNCTION_TYPE, MagicalJewelry.getId("set_jewel_nbt"), SET_JEWEL_NBT_FUNCTION);
		Registry.register(Registry.LOOT_FUNCTION_TYPE, MagicalJewelry.getId("set_jewel_nbt_boss"), SET_JEWEL_NBT_BOSS_FUNCTION);
	}
}