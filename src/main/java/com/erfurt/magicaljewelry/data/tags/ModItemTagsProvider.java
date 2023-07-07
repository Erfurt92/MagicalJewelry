package com.erfurt.magicaljewelry.data.tags;

import com.erfurt.magicaljewelry.MagicalJewelry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.erfurt.magicaljewelry.data.tags.ModTagsBuilder.Items.*;
import static com.erfurt.magicaljewelry.init.ItemInit.*;

public class ModItemTagsProvider extends ItemTagsProvider
{
    public ModItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper, BlockTagsProvider blockTags)
    {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), MagicalJewelry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(NECKLACE).add(GOLD_AMULET.get(), SILVER_AMULET.get());
        tag(RING).add(GOLD_RING.get(), SILVER_RING.get(), THE_ONE_RING.get());
        tag(BRACELET).add(GOLD_BRACELET.get(), SILVER_BRACELET.get());

        tag(PIGLIN_LOVED).add(GOLD_AMULET.get(), GOLD_BRACELET.get(), GOLD_RING.get(), THE_ONE_RING.get());
    }
}