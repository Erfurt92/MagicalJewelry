package com.erfurt.magicaljewelry.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class JewelAmuletModel<T extends LivingEntity> extends EntityModel<T>
{
    private static final String AMULET = "amulet";

    public ModelPart jewelAmulet;

    public JewelAmuletModel(ModelPart part)
    {
        super(RenderType::entityCutoutNoCull);
        this.jewelAmulet = part.getChild(AMULET);
        this.jewelAmulet.setRotation(0.0F, 0.0F, 0.0F);
    }

    public static LayerDefinition createLayer()
    {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild(AMULET, CubeListBuilder.create()
                .texOffs(0,0)
                .addBox(-2.0F, 2.0F, -3.125F, 4.0F, 4.0F, 1.0F), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }


    @Override
    public void renderToBuffer(PoseStack poseStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
        this.jewelAmulet.render(poseStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}