package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.render.model.JewelAmuletModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.common.capability.CapCurioItem;

public class JewelAmuletItem extends JewelItem
{
    public static final ResourceLocation GOLD_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gold_amulet.png");
    public static final ResourceLocation SILVER_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/silver_amulet.png");
    public static final ResourceLocation GEM_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gem_amulet.png");

    public JewelAmuletItem()
    {
        super();
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
    {
        return CapCurioItem.createProvider(new ICurio()
        {
            private Object amuletModel;

            @Override
            public boolean hasRender(String identifier, LivingEntity livingEntity)
            {
                return true;
            }

            @Override
            public void render(String identifier, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
            {
                RenderHelper.translateIfSneaking(matrixStack, livingEntity);
                RenderHelper.rotateIfSneaking(matrixStack, livingEntity);
                if(!(this.amuletModel instanceof JewelAmuletModel))
                {
                    this.amuletModel = new JewelAmuletModel();
                }

                JewelAmuletModel<?> jewelAmuletModel = (JewelAmuletModel)this.amuletModel;
                IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, jewelAmuletModel.getRenderType(getRenderType(stack)), false, stack.hasEffect());
                jewelAmuletModel.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                float[] i = DyeColor.byTranslationKey(getGemColor(stack), DyeColor.WHITE).getColorComponentValues();
                float red = i[0];
                float green = i[1];
                float blue = i[2];

                IVertexBuilder vertexBuilderGem = ItemRenderer.getBuffer(renderTypeBuffer, jewelAmuletModel.getRenderType(GEM_AMULET_TEXTURE), false, stack.hasEffect());
                jewelAmuletModel.render(matrixStack, vertexBuilderGem, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
            }

            @Override
            public void onCurioTick(String identifier, int index, LivingEntity livingEntity)
            {
                if(!livingEntity.getEntityWorld().isRemote && livingEntity.ticksExisted % 199 == 0 && !totalJewelEffects.isEmpty()) updateJewelEffects(stack, livingEntity, false);
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
            public boolean canRightClickEquip()
            {
                return true;
            }
        });
    }

    private static ResourceLocation getRenderType(ItemStack stack)
    {
        if(stack.getItem() == ItemInit.GOLD_AMULET.get().getItem()) return GOLD_AMULET_TEXTURE;
        return SILVER_AMULET_TEXTURE;
    }
}