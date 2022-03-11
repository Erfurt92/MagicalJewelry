package com.erfurt.magicaljewelry.data.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTagsBuilder
{
    public static final class Items
    {
        public static final TagKey<Item> NECKLACE = curios("necklace");
        public static final TagKey<Item> RING = curios("ring");
        public static final TagKey<Item> BRACELET = curios("bracelet");

        public static final TagKey<Item> PIGLIN_LOVED = minecraft("piglin_loved");

        private static TagKey<Item> curios(String path)
        {
            return ItemTags.create(new ResourceLocation("curios", path));
        }

        private static TagKey<Item> minecraft(String path)
        {
            return ItemTags.create(new ResourceLocation("minecraft", path));
        }
    }
}