package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class WaterEntityCondition implements LootItemCondition
{
    public static final Serializer SERIALIZER = new Serializer();

    public WaterEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        return entity instanceof WaterAnimal;
    }

    @Override
    public LootItemConditionType getType()
    {
        return LootInit.WATER_ENTITY_CONDITION;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<WaterEntityCondition>
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