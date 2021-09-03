package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class HostileEntityCondition implements LootItemCondition
{
    public static final Serializer SERIALIZER = new Serializer();

    public HostileEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);

        return entity instanceof Enemy && entity.canChangeDimensions() && !(entity instanceof Guardian || entity instanceof Drowned);
    }

    @Override
    public LootItemConditionType getType()
    {
        return LootInit.HOSTILE_ENTITY_CONDITION;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<HostileEntityCondition>
    {
        @Override
        public void serialize(JsonObject json, HostileEntityCondition value, JsonSerializationContext context) { }

        @Override
        public HostileEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new HostileEntityCondition();
        }
    }
}