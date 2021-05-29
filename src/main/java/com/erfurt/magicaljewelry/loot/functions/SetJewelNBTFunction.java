package com.erfurt.magicaljewelry.loot.functions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.util.interfaces.IJewelNBTHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.SetCount;

public class SetJewelNBTFunction extends LootFunction implements IJewelNBTHandler
{
	public static final Serializer SERIALIZER = new Serializer();

	private static String rarityID;
	
	private SetJewelNBTFunction(ILootCondition[] conditionIn)
	{
		super(conditionIn);
	}

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context)
	{
		rarityID = IJewelRarity.getRarity();

		IJewelNBTHandler.setJewelNBTData(stack, rarityID);

		return stack;
	}

	public static LootFunction.Builder<?> builder()
	{
		return builder(SetJewelNBTFunction::new);
	}

	@Override
	public LootFunctionType getFunctionType()
	{
		return LootInit.SET_JEWEL_NBT_FUNCTION;
	}

	public static class Serializer extends LootFunction.Serializer<SetJewelNBTFunction>
	{
        @Override
        public void serialize(JsonObject object, SetJewelNBTFunction functionClazz, JsonSerializationContext serializationContext)
        {
            object.addProperty("Rarity", rarityID);
        }

        @Override
        public SetJewelNBTFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
        {
            return new SetJewelNBTFunction(conditionsIn);
        }
    }
}