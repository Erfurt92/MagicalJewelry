package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraft.item.ItemStack;

import java.util.Random;
import java.util.UUID;

public interface IJewel extends IJewelEffects, IJewelRarity, IJewelAttributes
{
    Random rand = new Random();

    default ItemStack setMissingNBTData(ItemStack stack)
    {
        if(stack.getTag() != null)
        {
            if(!stack.getTag().contains(JewelItem.NBT_ATTRIBUTES))
            {
                JewelItem.setJewelAttributes(stack, getAttributes());
            }
            if(!stack.getTag().contains(JewelItem.NBT_UUID))
            {
                JewelItem.setJewelUUID(stack, UUID.randomUUID().toString());
            }
            if(!stack.getTag().contains(JewelItem.NBT_LEGENDARY_EFFECT))
            {
                JewelItem.setJewelLegendaryEffect(stack, rand.nextInt(legendaryEffectsList.size()));
            }
        }

        return stack;
    }
}