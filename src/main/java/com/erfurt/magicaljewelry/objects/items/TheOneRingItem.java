package com.erfurt.magicaljewelry.objects.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.common.capability.CapCurioItem;

import java.util.List;

public class TheOneRingItem extends Item
{
    public TheOneRingItem()
    {
        super(new Item.Properties().maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return CapCurioItem.createProvider(new ICurio()
        {
            @Override
            public void onCurioTick(String identifier, int index, LivingEntity livingEntity)
            {
                if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0) livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onEquipped(String identifier, LivingEntity livingEntity)
            {
                livingEntity.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, false, true));
            }

            @Override
            public void onUnequipped(String identifier, LivingEntity livingEntity)
            {
                livingEntity.removePotionEffect(Effects.INVISIBILITY);
            }

            @Override
            public boolean canRightClickEquip()
            {
                return true;
            }
        });
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        tooltip.set(0, tooltip.get(0).applyTextStyle(TextFormatting.GOLD));

        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg durbatul\u00FBk.").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg gimbatul.").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "Ash nazg thrakatul\u00FBk").applyTextStyle(TextFormatting.DARK_RED));
        tooltip.add(new StringTextComponent(TextFormatting.ITALIC + "agh burzum-ishi krimpatul.").applyTextStyle(TextFormatting.DARK_RED));
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
