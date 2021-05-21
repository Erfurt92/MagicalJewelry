package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class WaterEntityCondition implements ILootCondition
{
    public static final Serializer SERIALIZER = new Serializer();

    private WaterEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        return entity instanceof WaterMobEntity;
    }

    @Override
    public LootConditionType getConditionType()
    {
        return LootInit.WATER_ENTITY_CONDITION;
    }

    public static class Serializer implements ILootSerializer<WaterEntityCondition>
    {
        @Override
        public void serialize(JsonObject json, WaterEntityCondition value, JsonSerializationContext context) { }

        @Override
        public WaterEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new WaterEntityCondition();
        }
    }
}