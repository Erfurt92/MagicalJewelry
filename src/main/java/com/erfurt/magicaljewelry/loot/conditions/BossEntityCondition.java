package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BossEntityCondition implements LootItemCondition
{
    public static final Serializer SERIALIZER = new Serializer();

    public BossEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        return entity instanceof Enemy && !entity.canChangeDimensions();
    }

    @Override
    public LootItemConditionType getType()
    {
        return LootInit.BOSS_ENTITY_CONDITION;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BossEntityCondition>
    {
        @Override
        public void serialize(JsonObject json, BossEntityCondition value, JsonSerializationContext context) { }

        @Override
        public BossEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new BossEntityCondition();
        }
    }
}