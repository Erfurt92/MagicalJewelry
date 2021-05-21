package com.erfurt.magicaljewelry.data.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTagsBuilder
{
    public static final class Items
    {
        public static final ITag.INamedTag<Item> NECKLACE = curios("necklace");
        public static final ITag.INamedTag<Item> RING = curios("ring");
        public static final ITag.INamedTag<Item> BRACELET = curios("bracelet");

        private static ITag.INamedTag<Item> curios(String path)
        {
            return ItemTags.makeWrapperTag(new ResourceLocation("curios", path).toString());
        }
    }
}