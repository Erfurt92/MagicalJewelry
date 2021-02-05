package com.erfurt.magicaljewelry.loot.functions;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.util.interfaces.IJewelNBTHandler;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class SetJewelNBTBossFunction extends LootFunction implements IJewelNBTHandler
{
	private static String rarityID;
	
	private SetJewelNBTBossFunction(ILootCondition[] conditionIn)
	{
		super(conditionIn);
	}

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context)
	{
		rarityID = getRarityBoss();

		setJewelNBTData(stack, rarityID);

		return stack;
	}
	
	public static class Serializer extends LootFunction.Serializer<SetJewelNBTBossFunction>
	{
        public static final Serializer INSTANCE = new Serializer(new ResourceLocation(MagicalJewelry.MOD_ID, "set_jewel_nbt_boss"), SetJewelNBTBossFunction.class);

        Serializer(ResourceLocation location, Class<SetJewelNBTBossFunction> clazz)
        {
            super(location, clazz);
        }

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