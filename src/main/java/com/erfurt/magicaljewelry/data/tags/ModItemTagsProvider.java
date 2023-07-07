package com.erfurt.magicaljewelry.data.tags;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper, BlockTagsProvider blockTags)
    {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), MagicalJewelry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(ModTagsBuilder.Items.NECKLACE).add(ItemInit.GOLD_AMULET.get(), ItemInit.SILVER_AMULET.get());
        tag(ModTagsBuilder.Items.RING).add(ItemInit.GOLD_RING.get(), ItemInit.SILVER_RING.get(), ItemInit.THE_ONE_RING.get());
        tag(ModTagsBuilder.Items.BRACELET).add(ItemInit.GOLD_BRACELET.get(), ItemInit.SILVER_BRACELET.get());

        tag(ModTagsBuilder.Items.PIGLIN_LOVED).add(ItemInit.GOLD_AMULET.get(), ItemInit.GOLD_BRACELET.get(), ItemInit.GOLD_RING.get(), ItemInit.THE_ONE_RING.get());
    }
}