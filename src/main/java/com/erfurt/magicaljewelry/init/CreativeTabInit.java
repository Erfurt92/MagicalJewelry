package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeTabInit
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicalJewelry.MOD_ID);

    public static final RegistryObject<CreativeModeTab> GROUP = TABS.register("magicaljewelry_group", () -> CreativeModeTab.builder().icon(ItemInit.GOLD_RING.get()::getDefaultInstance).title(Component.translatable("itemGroup.magicaljewelry_group")).build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTab() == GROUP.get())
        {
            for(RegistryObject<Item> item : ItemInit.ITEMS.getEntries())
            {
                if(item != ItemInit.THE_ONE_RING)
                {
                    if(item == ItemInit.JEWEL_UPGRADE_SMITHING_TEMPLATE)
                    {
                        event.accept(ItemInit.JEWEL_UPGRADE_SMITHING_TEMPLATE);
                    }
                    else
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
}