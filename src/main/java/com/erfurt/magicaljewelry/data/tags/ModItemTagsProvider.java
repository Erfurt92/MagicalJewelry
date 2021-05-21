package com.erfurt.magicaljewelry.data.tags;

import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
    }

    @Override
    protected void registerTags()
    {
        getBuilder(TagsInit.Items.NECKLACE).add(ItemInit.GOLD_AMULET.get(), ItemInit.SILVER_AMULET.get());
        getBuilder(TagsInit.Items.RING).add(ItemInit.GOLD_RING.get(), ItemInit.SILVER_RING.get(), ItemInit.THE_ONE_RING.get());
        getBuilder(TagsInit.Items.BRACELET).add(ItemInit.GOLD_BRACELET.get(), ItemInit.SILVER_BRACELET.get());
    }
}