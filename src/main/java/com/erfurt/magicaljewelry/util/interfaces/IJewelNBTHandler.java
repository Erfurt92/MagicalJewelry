package com.erfurt.magicaljewelry.util.interfaces;

import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import java.util.Random;
import java.util.UUID;

public interface IJewelNBTHandler extends IJewelEffects, IJewelRarity, IJewelAttributes
{
    Random rand = new Random();

    static ItemStack setJewelNBTData(ItemStack stack, String rarityID)
    {
        JewelItem.setJewelRarity(stack, rarityID);

        JewelItem.setJewelEffects(stack, IJewelEffects.getEffects(rarityID, JewelItem.jewelEffects));

        JewelItem.setJewelLegendaryEffect(stack, rand.nextInt(legendaryEffectsList.size()));

        JewelItem.setJewelAttributes(stack, IJewelAttributes.getAttributes());

        JewelItem.setJewelUUID(stack, UUID.randomUUID().toString());

        int colorID = rand.nextInt(DyeColor.values().length);
        String gemColor = DyeColor.byId(colorID).getSerializedName();
        JewelItem.setGemColor(stack, gemColor);

        return stack;
    }
}