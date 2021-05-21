package com.erfurt.magicaljewelry.data.recipes;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.recipes.JewelUpgradeRecipe;
import com.erfurt.magicaljewelry.recipes.UpgradeNBTIngredient;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class JewelUpgradeRecipeBuilder
{
    private final Item addition;
    private final Item jewel;
    private final String rarityIn;
    private final String rarityOut;
    private final IRecipeSerializer<?> serializer;

    public JewelUpgradeRecipeBuilder(IRecipeSerializer<?> serializer, Item addition, Item jewel, String rarityIn, String rarityOut)
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

    public void build(Consumer<IFinishedRecipe> consumer, String id)
    {
        this.build(consumer, MagicalJewelry.getId(id + "_upgrade_" + rarityIn + "_to_" + rarityOut));
    }

    public void build(Consumer<IFinishedRecipe> recipe, ResourceLocation id)
    {
        recipe.accept(new JewelUpgradeRecipeBuilder.Result(id, this.serializer, this.addition, this.jewel, this.rarityIn, this.rarityOut));
    }

    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final Ingredient addition;
        private final Item jewel;
        private final String rarityIn;
        private final String rarityOut;
        private final IRecipeSerializer<?> serializer;

        public Result(ResourceLocation id, IRecipeSerializer<?> serializer, Item addition, Item jewel, String rarityIn, String rarityOut)
        {
            this.id = id;
            this.serializer = serializer;
            this.addition = Ingredient.fromItems(addition);
            this.jewel = jewel;;
            this.rarityIn = rarityIn;
            this.rarityOut = rarityOut;
        }

        public void serialize(JsonObject json)
        {
            JsonObject jsonBase = new JsonObject();
            jsonBase.addProperty("type", CraftingHelper.getID(UpgradeNBTIngredient.Serializer.INSTANCE).toString());
            jsonBase.addProperty("item", this.jewel.getRegistryName().toString());
            JsonObject jsonNBTin = new JsonObject();
            jsonNBTin.addProperty(JewelItem.NBT_RARITY, rarityIn);
            jsonBase.add("nbt", jsonNBTin);
            json.add("base", jsonBase);

            json.add("addition", this.addition.serialize());

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
        public ResourceLocation getID()
        {
            return this.id;
        }

        public IRecipeSerializer<?> getSerializer()
        {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson()
        {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID()
        {
            return null;
        }
    }
}