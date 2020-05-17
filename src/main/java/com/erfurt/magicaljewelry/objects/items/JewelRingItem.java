package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.common.capability.CapCurioItem;

public class JewelRingItem extends JewelItem
{
    public JewelRingItem()
    {
        super();
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return CapCurioItem.createProvider(new ICurio()
        {
            @Override
            public void onCurioTick(String identifier, int index, LivingEntity livingEntity)
            {
                if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0 && !totalJewelEffects.isEmpty())
                {
                    if(!MagicalJewelryConfigBuilder.JEWEL_ATTRIBUTES.get()) livingEntity.getAttributes().removeAttributeModifiers(jewelAttributesForRemoval);

                    updateJewelEffects(stack, livingEntity, false);
                }
            }

            @Override
            public void onEquipped(String identifier, LivingEntity livingEntity)
            {
                getTotalJewelEffects(stack);
                updateJewelEffects(stack, livingEntity, false);
            }

            @Override
            public void onUnequipped(String identifier, LivingEntity livingEntity)
            {
                updateJewelEffects(stack, livingEntity, true);
            }

            @Override
            public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier)
            {
                Multimap<String, AttributeModifier> attributes = HashMultimap.create();
                updateJewelAttributes(stack, attributes);

                return attributes;
            }

            @Override
            public boolean canRightClickEquip()
            {
                return true;
            }
        });
    }
}