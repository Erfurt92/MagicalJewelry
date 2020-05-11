package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class BossEntityCondition implements ILootCondition
{

    private BossEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        if(entity instanceof IMob && !entity.isNonBoss()) return true;
        else return false;
    }

    public static class Serializer extends AbstractSerializer<BossEntityCondition>
    {
        public Serializer()
        {
            super(MagicalJewelry.getId("is_boss_entity"), BossEntityCondition.class);
        }

        @Override
        public void serialize(JsonObject json, BossEntityCondition value, JsonSerializationContext context) { }

        @Override
        public BossEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new BossEntityCondition();
        }
    }
}