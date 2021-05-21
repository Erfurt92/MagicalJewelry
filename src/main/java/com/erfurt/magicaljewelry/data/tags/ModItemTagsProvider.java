package com.erfurt.magicaljewelry.data.tags;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, MagicalJewelry.MOD_ID, existingFileHelper), MagicalJewelry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags()
    {
        getOrCreateBuilder(ModTagsBuilder.Items.NECKLACE).add(ItemInit.GOLD_AMULET.get(), ItemInit.SILVER_AMULET.get());
        getOrCreateBuilder(ModTagsBuilder.Items.RING).add(ItemInit.GOLD_RING.get(), ItemInit.SILVER_RING.get(), ItemInit.THE_ONE_RING.get());
        getOrCreateBuilder(ModTagsBuilder.Items.BRACELET).add(ItemInit.GOLD_BRACELET.get(), ItemInit.SILVER_BRACELET.get());
    }
}