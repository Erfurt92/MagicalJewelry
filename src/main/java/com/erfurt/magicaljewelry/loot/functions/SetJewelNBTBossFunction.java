package com.erfurt.magicaljewelry.loot.functions;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.erfurt.magicaljewelry.util.interfaces.IJewelAttributes;
import com.erfurt.magicaljewelry.util.interfaces.IJewelEffects;
import com.erfurt.magicaljewelry.util.interfaces.IJewelRarity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

import java.util.Random;
import java.util.UUID;

public class SetJewelNBTBossFunction extends LootFunction implements IJewelEffects, IJewelRarity, IJewelAttributes
{
	Random rand = new Random();

	private static String rarityID;
	
	private static String gemColor;
	
	private SetJewelNBTBossFunction(ILootCondition[] conditionIn)
	{
		super(conditionIn);
	}

	@Override
	public ItemStack doApply(ItemStack stack, LootContext context)
	{
		rarityID = getRarityBoss();

		JewelItem.setJewelRarity(stack, rarityID);
		
		JewelItem.setJewelEffects(stack, getEffects(rarityID, JewelItem.jewelEffects));

		if(rarityID.equals(JewelRarity.LEGENDARY.getName())) JewelItem.setJewelLegendaryEffect(stack, rand.nextInt(legendaryEffectsList.size() - 1) + 1);

		JewelItem.setJewelAttributes(stack, getAttributes());

		JewelItem.setJewelUUID(stack, UUID.randomUUID().toString());

		int colorID = new Random().nextInt(DyeColor.values().length);
		gemColor = DyeColor.byId(colorID).getName();
		JewelItem.setGemColor(stack, gemColor);

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
            object.addProperty("GemColor", gemColor);
        }

        @Override
        public SetJewelNBTBossFunction deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn)
        {
            return new SetJewelNBTBossFunction(conditionsIn);
        }
    }
}