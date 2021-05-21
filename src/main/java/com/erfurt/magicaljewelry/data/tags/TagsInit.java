package com.erfurt.magicaljewelry.data.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class TagsInit
{
    public static final class Items
    {
        public static final Tag<Item> NECKLACE = curios("necklace");
        public static final Tag<Item> RING = curios("ring");
        public static final Tag<Item> BRACELET = curios("bracelet");

        private static Tag<Item> curios(String path)
        {
            return new ItemTags.Wrapper(new ResourceLocation("curios", path));
        }
    }
}