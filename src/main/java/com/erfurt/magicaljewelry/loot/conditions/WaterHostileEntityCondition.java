package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class WaterHostileEntityCondition implements ILootCondition
{
    public static final Serializer SERIALIZER = new WaterHostileEntityCondition.Serializer();

    public WaterHostileEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        return entity instanceof GuardianEntity || entity instanceof DrownedEntity;
    }

    @Override
    public LootConditionType getConditionType()
    {
        return LootInit.WATER_HOSTILE_ENTITY_CONDITION;
    }

    public static class Serializer implements ILootSerializer<WaterHostileEntityCondition>
    {
        @Override
        public void serialize(JsonObject json, WaterHostileEntityCondition value, JsonSerializationContext context) { }

        @Override
        public WaterHostileEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new WaterHostileEntityCondition();
        }
    }
}
