package com.erfurt.magicaljewelry.recipes;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.util.config.MagicalJewelryConfigBuilder;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class JewelUpgradeRecipe extends UpgradeRecipe implements Recipe<Container>
{
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;
    private final ResourceLocation recipeId;

    public JewelUpgradeRecipe(ResourceLocation recipeId, Ingredient base, Ingredient addition, ItemStack result)
    {
        super(recipeId, base, addition, result);
        this.recipeId = recipeId;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
    public boolean matches(Container inv, Level worldIn)
    {
        ItemStack stackIn = inv.getItem(0);
        ItemStack stackOut = this.result.copy();
        boolean rarityInCheck = false;
        if(stackIn.getItem() instanceof JewelItem) rarityInCheck = JewelRarity.containsRarity(JewelItem.getJewelRarity(stackIn));
        boolean rarityOutCheck = false;
        if(stackOut.getItem() instanceof JewelItem) rarityOutCheck = JewelRarity.containsRarity(JewelItem.getJewelRarity(stackOut));
        boolean upgradeEnabled = MagicalJewelryConfigBuilder.JEWEL_UPGRADE_DISABLE.get();
        return this.base.test(stackIn) && this.addition.test(inv.getItem(1)) && rarityInCheck && rarityOutCheck && !upgradeEnabled;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack assemble(Container inv)
    {
        ItemStack stackOut = this.result.copy();
        ItemStack stackIn = inv.getItem(0);
        if(stackIn.hasTag() && stackOut.hasTag())
        {
            String rarityOut = stackOut.getTag().get(JewelItem.NBT_RARITY).getAsString();
            CompoundTag nbtIn = stackIn.getTag();

            stackOut.setTag(nbtIn.copy());
            JewelItem.setJewelRarity(stackOut, rarityOut);
            stackOut.getTag().remove("Damage");
        }

        return stackOut;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    @Override
    public ItemStack getResultItem()
    {
        return this.result;
    }

    @Override
    public ItemStack getToastSymbol()
    {
        return new ItemStack(Blocks.SMITHING_TABLE);
    }

    @Override
    public ResourceLocation getId()
    {
        return this.recipeId;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return Serializer.SERIALIZER;
    }

    @Override
    public RecipeType<?> getType()
    {
        return RecipeType.SMITHING;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<JewelUpgradeRecipe>
    {
        public static final Serializer SERIALIZER = new Serializer();

        static
        {
            SERIALIZER.setRegistryName(MagicalJewelry.getId("jewel_upgrade"));
        }

        public JewelUpgradeRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient ingredient = UpgradeNBTIngredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "addition"));
            ItemStack stackOut = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new JewelUpgradeRecipe(recipeId, ingredient, ingredient1, stackOut);
        }

        public JewelUpgradeRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
        {
            Ingredient ingredient = UpgradeNBTIngredient.fromNetwork(buffer);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            ItemStack stackOut = buffer.readItem();
            return new JewelUpgradeRecipe(recipeId, ingredient, ingredient1, stackOut);
        }

        public void toNetwork(FriendlyByteBuf buffer, JewelUpgradeRecipe recipe)
        {
            recipe.base.toNetwork(buffer);
            recipe.addition.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}