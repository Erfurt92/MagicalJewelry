package com.erfurt.magicaljewelry.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JewelModifier extends LootModifier
{
    public static final Supplier<Codec<JewelModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(Codec.STRING.fieldOf("add_loot_table").forGetter(m -> String.valueOf(m.lootTable)))
            .apply(inst, JewelModifier::new)
    ));
    private final ResourceLocation lootTable;

    public JewelModifier(LootItemCondition[] lootConditions, String lootTable)
    {
        super(lootConditions);
        this.lootTable = ResourceLocation.tryParse(lootTable);
    }

    boolean doubleEntryPrevention = false;

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
    {
        if(doubleEntryPrevention) return generatedLoot;

        doubleEntryPrevention = true;
        LootTable lootTable = context.getResolver().getLootTable(this.lootTable);
        lootTable.getRandomItems(context, generatedLoot::add);
        doubleEntryPrevention = false;

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec()
    {
        return CODEC.get();
    }
}