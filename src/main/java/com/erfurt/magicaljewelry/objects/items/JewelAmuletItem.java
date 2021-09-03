package com.erfurt.magicaljewelry.objects.items;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import com.erfurt.magicaljewelry.render.JewelLayerDefinitions;
import com.erfurt.magicaljewelry.render.model.JewelAmuletModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class JewelAmuletItem extends JewelItem implements ICurioRenderer
{
    private Object model;

    private static final ResourceLocation GOLD_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gold_amulet.png");
    private static final ResourceLocation SILVER_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/silver_amulet.png");
    private static final ResourceLocation GEM_AMULET_TEXTURE = MagicalJewelry.getId( "textures/models/amulet/gem_amulet.png");

    public JewelAmuletItem()
    {
        super();
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int light,
                                                                          float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch)
    {
        LivingEntity livingEntity = slotContext.entity();
        ICurioRenderer.translateIfSneaking(matrixStack, livingEntity);
        ICurioRenderer.rotateIfSneaking(matrixStack, livingEntity);

        if(this.model == null) this.model = new JewelAmuletModel(Minecraft.getInstance().getEntityModels().bakeLayer(JewelLayerDefinitions.AMULET));

        if(this.model instanceof JewelAmuletModel)
        {
            JewelAmuletModel<?> jewelAmuletModel = (JewelAmuletModel) this.model;
            VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(renderTypeBuffer, jewelAmuletModel.renderType(getRenderType(stack)), false, stack.hasFoil());
            jewelAmuletModel.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

            float[] i = DyeColor.byName(getGemColor(stack), DyeColor.WHITE).getTextureDiffuseColors();
            float red = i[0];
            float green = i[1];
            float blue = i[2];

            VertexConsumer vertexBuilderGem = ItemRenderer.getFoilBuffer(renderTypeBuffer, jewelAmuletModel.renderType(GEM_AMULET_TEXTURE), false, stack.hasFoil());
            jewelAmuletModel.renderToBuffer(matrixStack, vertexBuilderGem, light, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
        }
    }

    private static ResourceLocation getRenderType(ItemStack stack)
    {
        if(stack.getItem() == ItemInit.GOLD_AMULET.get().asItem()) return GOLD_AMULET_TEXTURE;
        return SILVER_AMULET_TEXTURE;
    }
}