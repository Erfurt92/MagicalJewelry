package com.erfurt.magicaljewelry.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class JewelModifier extends LootModifier
{
    private final ResourceLocation lootTable;

    public JewelModifier(ILootCondition[] lootConditions, ResourceLocation lootTable)
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
        List<ItemStack> loot = lootTable.generate(context);
        generatedLoot.addAll(loot);
        doubleEntryPrevention = false;

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<JewelModifier>
    {
        @Override
        public JewelModifier read(ResourceLocation location, JsonObject object, ILootCondition[] iLootCondition)
        {
            ResourceLocation lootTable = new ResourceLocation(JSONUtils.getString(object, "add_loot_table"));
            return new JewelModifier(iLootCondition, lootTable);
        }

        @Override
        public JsonObject write(JewelModifier instance)
        {
            return makeConditions(instance.conditions);
        }
    }
}