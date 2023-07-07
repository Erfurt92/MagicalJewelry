package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.erfurt.magicaljewelry.MagicalJewelry.getId;

public class CreativeTabInit
{
    public static CreativeModeTab GROUP;

    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event)
    {
        GROUP = event.registerCreativeModeTab(getId("magicaljewelry_group"), builder -> builder.icon(() -> new ItemStack(ItemInit.GOLD_RING.get())).title(Component.translatable("itemGroup.magicaljewelry_group")).build());
    }

    public static void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if(event.getTab() == GROUP)
        {
            for(RegistryObject<Item> item : ItemInit.ITEMS.getEntries())
            {
                if(item != ItemInit.THE_ONE_RING)
                {
                    for (DyeColor color : DyeColor.values())
                    {
                        ItemStack stack = new ItemStack(item.get());
                        JewelItem.setGemColor(stack, color.getSerializedName());
                        event.accept(stack);
                    }
                }
            }
        }
    }
}