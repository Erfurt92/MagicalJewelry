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

public class HostileEntityCondition implements ILootCondition
{

    private HostileEntityCondition()
    {

    }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        System.out.println(entity);

        if(entity instanceof IMob && entity.isNonBoss())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static class Serializer extends ILootCondition.AbstractSerializer<HostileEntityCondition>
    {
        public Serializer()
        {
            super(MagicalJewelry.getId("is_hostile_entity"), HostileEntityCondition.class);
        }

        @Override
        public void serialize(JsonObject json, HostileEntityCondition value, JsonSerializationContext context)
        {

        }

        @Override
        public HostileEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {

            return new HostileEntityCondition();
        }
    }
}