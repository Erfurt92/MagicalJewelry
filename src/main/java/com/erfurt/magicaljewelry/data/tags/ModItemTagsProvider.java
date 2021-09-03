package com.erfurt.magicaljewelry.data.tags;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, MagicalJewelry.MOD_ID, existingFileHelper), MagicalJewelry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        tag(ModTagsBuilder.Items.NECKLACE).add(ItemInit.GOLD_AMULET.get(), ItemInit.SILVER_AMULET.get());
        tag(ModTagsBuilder.Items.RING).add(ItemInit.GOLD_RING.get(), ItemInit.SILVER_RING.get(), ItemInit.THE_ONE_RING.get());
        tag(ModTagsBuilder.Items.BRACELET).add(ItemInit.GOLD_BRACELET.get(), ItemInit.SILVER_BRACELET.get());

        tag(ModTagsBuilder.Items.PIGLIN_LOVED).add(ItemInit.GOLD_AMULET.get(), ItemInit.GOLD_BRACELET.get(), ItemInit.GOLD_RING.get(), ItemInit.THE_ONE_RING.get());
    }
}