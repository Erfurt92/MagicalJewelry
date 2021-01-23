package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.init.LootInit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

public class BossEntityCondition implements ILootCondition
{
    public static final Serializer SERIALIZER = new Serializer();

    private BossEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        if(entity instanceof IMob && !entity.isNonBoss()) return true;
        else return false;
    }

    @Override
    public LootConditionType func_230419_b_()
    {
        return LootInit.BOSS_ENTITY_CONDITION;
    }

    public static class Serializer implements ILootSerializer<BossEntityCondition>
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