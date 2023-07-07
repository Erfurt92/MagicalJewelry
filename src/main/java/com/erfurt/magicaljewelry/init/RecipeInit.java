package com.erfurt.magicaljewelry.init;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.recipes.JewelUpgradeRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MagicalJewelry.MOD_ID);

    public static final RegistryObject<RecipeSerializer<JewelUpgradeRecipe>> JEWEL_UPGRADE_RECIPE = SERIALIZERS.register("jewel_upgrade", () -> JewelUpgradeRecipe.Serializer.SERIALIZER);
}