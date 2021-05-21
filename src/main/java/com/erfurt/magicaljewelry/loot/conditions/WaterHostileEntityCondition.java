package com.erfurt.magicaljewelry.loot.conditions;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

public class WaterHostileEntityCondition implements ILootCondition
{
    private WaterHostileEntityCondition() { }

    @Override
    public boolean test(LootContext context)
    {
        Entity entity = context.get(LootParameters.THIS_ENTITY);

        return entity instanceof GuardianEntity || entity instanceof DrownedEntity;
    }

    public static class Serializer extends AbstractSerializer<WaterHostileEntityCondition>
    {
        public Serializer()
        {
            super(MagicalJewelry.getId("is_water_hostile_entity"), WaterHostileEntityCondition.class);
        }

        @Override
        public void serialize(JsonObject json, WaterHostileEntityCondition value, JsonSerializationContext context) { }

        @Override
        public WaterHostileEntityCondition deserialize(JsonObject json, JsonDeserializationContext context)
        {
            return new WaterHostileEntityCondition();
        }
    }
}
