package com.erfurt.magicaljewelry.render.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class JewelAmuletModel<T extends LivingEntity> extends EntityModel<T>
{
    public ModelRenderer jewelAmulet;

    public JewelAmuletModel()
    {
        this.textureWidth = 16;
        this.textureHeight = 16;
        this.jewelAmulet = new ModelRenderer(this, 0, 0);
        this.jewelAmulet.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.jewelAmulet.addBox(-2.0F, 2.0F, -3.125F, 4.0F, 4.0F, 1.0F, 0.0F);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.jewelAmulet.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}