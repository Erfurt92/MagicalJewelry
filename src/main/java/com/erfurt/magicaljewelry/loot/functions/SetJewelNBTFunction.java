package com.erfurt.magicaljewelry.loot.functions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.erfurt.magicaljewelry.util.interfaces.IJewelNBTHandler;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetJewelNBTFunction extends LootItemConditionalFunction implements IJewelNBTHandler
{
	public static final Serializer SERIALIZER = new Serializer();

	private static String rarityID;
	
	private SetJewelNBTFunction(LootItemCondition[] conditionIn)
	{
		super(conditionIn);
	}

	@Override
	public ItemStack run(ItemStack stack, LootContext context)
	{
		Entity entity = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
		rarityID = IJewelRarity.getRarity(entity);

		IJewelNBTHandler.setJewelNBTData(stack, rarityID);

		return stack;
	}

	public static LootItemConditionalFunction.Builder<?> builder()
	{
		return simpleBuilder(SetJewelNBTFunction::new);
	}

	@Override
	public LootItemFunctionType getType()
	{
		return LootInit.SET_JEWEL_NBT_FUNCTION;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SetJewelNBTFunction>
	{
        @Override
        public void serialize(JsonObject object, SetJewelNBTFunction functionClazz, JsonSerializationContext serializationContext)
        {
            object.addProperty("Rarity", rarityID);
        }

        @Override
        public SetJewelNBTFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn)
        {
            return new SetJewelNBTFunction(conditionsIn);
        }
    }
}