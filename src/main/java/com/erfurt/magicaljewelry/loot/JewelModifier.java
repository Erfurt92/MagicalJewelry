package com.erfurt.magicaljewelry.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class JewelModifier extends LootModifier
{
    private final ResourceLocation lootTable;

    public JewelModifier(LootItemCondition[] lootConditions, ResourceLocation lootTable)
    {
        super(lootConditions);
        this.lootTable = lootTable;
    }

    boolean doubleEntryPrevention = false;

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        if(doubleEntryPrevention) return generatedLoot;

        doubleEntryPrevention = true;
        LootTable lootTable = context.getLootTable(this.lootTable);
        List<ItemStack> loot = lootTable.getRandomItems(context);
        generatedLoot.addAll(loot);
        doubleEntryPrevention = false;

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<JewelModifier>
    {
        String add_loot_table = "add_loot_table";

        @Override
        public JewelModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] iLootCondition)
        {
            ResourceLocation lootTable = new ResourceLocation(GsonHelper.getAsString(object, add_loot_table));
            return new JewelModifier(iLootCondition, lootTable);
        }

        @Override
        public JsonObject write(JewelModifier instance)
        {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty(add_loot_table, instance.lootTable.toString());
            return json;
        }
    }
}