package com.erfurt.magicaljewelry.loot.functions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.util.interfaces.IJewelNBTHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class SetJewelNBTBossFunction extends LootFunction implements IJewelNBTHandler
{
	public static final Serializer SERIALIZER = new Serializer();

	private static String rarityID;
	
	private SetJewelNBTBossFunction(ILootCondition[] conditionIn)
	{
		super(conditionIn);
	}

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context)
	{
		Entity entity = context.get(LootParameters.KILLER_ENTITY);
		rarityID = IJewelRarity.getRarityBoss((LivingEntity) entity);

		IJewelNBTHandler.setJewelNBTData(stack, rarityID);

		return stack;
	}

	public static LootFunction.Builder<?> builder()
	{
		return builder(SetJewelNBTBossFunction::new);
	}

	@Override
	public LootFunctionType getFunctionType()
	{
		return LootInit.SET_JEWEL_NBT_BOSS_FUNCTION;
	}

	public static class Serializer extends LootFunction.Serializer<SetJewelNBTBossFunction>
	{
        @Override
        public void serialize(JsonObject object, SetJewelNBTBossFunction functionClazz, JsonSerializationContext serializationContext)
        {
            object.addProperty("Rarity", rarityID);
        }

        @Override
        public SetJewelNBTBossFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
        {
            return new SetJewelNBTBossFunction(conditionsIn);
        }
    }
}