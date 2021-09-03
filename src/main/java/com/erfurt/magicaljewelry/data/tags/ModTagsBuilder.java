package com.erfurt.magicaljewelry.data.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class ModTagsBuilder
{
    public static final class Items
    {
        public static final Tag.Named<Item> NECKLACE = curios("necklace");
        public static final Tag.Named<Item> RING = curios("ring");
        public static final Tag.Named<Item> BRACELET = curios("bracelet");

        public static final Tag.Named<Item> PIGLIN_LOVED = minecraft("piglin_loved");

        private static Tag.Named<Item> curios(String path)
        {
            return ItemTags.bind(new ResourceLocation("curios", path).toString());
        }

        private static Tag.Named<Item> minecraft(String path)
        {
            return ItemTags.bind(new ResourceLocation("minecraft", path).toString());
        }
    }
}