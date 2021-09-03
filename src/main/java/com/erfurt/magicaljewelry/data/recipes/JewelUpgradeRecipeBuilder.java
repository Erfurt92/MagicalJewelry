package com.erfurt.magicaljewelry.data.recipes;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.recipes.JewelUpgradeRecipe;
import com.erfurt.magicaljewelry.recipes.UpgradeNBTIngredient;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class JewelUpgradeRecipeBuilder
{
    private final Item addition;
    private final Item jewel;
    private final String rarityIn;
    private final String rarityOut;
    private final RecipeSerializer<?> serializer;

    public JewelUpgradeRecipeBuilder(RecipeSerializer<?> serializer, Item addition, Item jewel, String rarityIn, String rarityOut)
    {
        this.serializer = serializer;
        this.addition = addition;
        this.jewel = jewel;
        this.rarityIn = rarityIn;
        this.rarityOut = rarityOut;
    }

    public static JewelUpgradeRecipeBuilder jewelUpgradeRecipe(Item jewel, Item addition, String rarityIn, String rarityOut)
    {
        return new JewelUpgradeRecipeBuilder(JewelUpgradeRecipe.Serializer.SERIALIZER, addition, jewel, rarityIn, rarityOut);
    }

    public void build(Consumer<FinishedRecipe> consumer, String id)
    {
        this.build(consumer, MagicalJewelry.getId(id + "_upgrade_" + rarityIn + "_to_" + rarityOut));
    }

    public void build(Consumer<FinishedRecipe> recipe, ResourceLocation id)
    {
        recipe.accept(new JewelUpgradeRecipeBuilder.Result(id, this.serializer, this.addition, this.jewel, this.rarityIn, this.rarityOut));
    }

    public static class Result implements FinishedRecipe
    {
        private final ResourceLocation id;
        private final Ingredient addition;
        private final Item jewel;
        private final String rarityIn;
        private final String rarityOut;
        private final RecipeSerializer<?> serializer;

        public Result(ResourceLocation id, RecipeSerializer<?> serializer, Item addition, Item jewel, String rarityIn, String rarityOut)
        {
            this.id = id;
            this.serializer = serializer;
            this.addition = Ingredient.of(addition);
            this.jewel = jewel;;
            this.rarityIn = rarityIn;
            this.rarityOut = rarityOut;
        }

        public void serializeRecipeData(JsonObject json)
        {
            JsonObject jsonBase = new JsonObject();
            jsonBase.addProperty("type", CraftingHelper.getID(UpgradeNBTIngredient.Serializer.INSTANCE).toString());
            jsonBase.addProperty("item", this.jewel.getRegistryName().toString());
            JsonObject jsonNBTin = new JsonObject();
            jsonNBTin.addProperty(JewelItem.NBT_RARITY, rarityIn);
            jsonBase.add("nbt", jsonNBTin);
            json.add("base", jsonBase);

            json.add("addition", this.addition.toJson());

            JsonObject jsonResult = new JsonObject();
            jsonResult.addProperty("item", this.jewel.getRegistryName().toString());
            JsonObject jsonNBTout = new JsonObject();
            jsonNBTout.addProperty(JewelItem.NBT_RARITY, rarityOut);
            jsonResult.add("nbt", jsonNBTout);
            json.add("result", jsonResult);
        }

        /**
         * Gets the ID for the recipe.
         */
        @Override
        public ResourceLocation getId()
        {
            return this.id;
        }

        public RecipeSerializer<?> getType()
        {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement()
        {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId()
        {
            return null;
        }
    }
}