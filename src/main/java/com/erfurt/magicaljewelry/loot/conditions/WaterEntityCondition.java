package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class WaterEntityCondition implements ILootCondition
{
    private WaterEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        return entity instanceof WaterMobEntity;
    }

    public static class Serializer extends AbstractSerializer<WaterEntityCondition>
    {
        public Serializer()
        {
            super(MagicalJewelry.getId("is_water_entity"), WaterEntityCondition.class);
        }

        @Override
        public void serialize(JsonObject json, WaterEntityCondition value, JsonSerializationContext context) { }

        @Override
        public WaterEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new WaterEntityCondition();
        }
    }
}