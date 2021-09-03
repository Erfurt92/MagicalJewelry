package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class WaterHostileEntityCondition implements LootItemCondition
{
    public static final Serializer SERIALIZER = new WaterHostileEntityCondition.Serializer();

    public WaterHostileEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        return entity instanceof Guardian || entity instanceof Drowned;
    }

    @Override
    public LootItemConditionType getType()
    {
        return LootInit.WATER_HOSTILE_ENTITY_CONDITION;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<WaterHostileEntityCondition>
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
