package com.erfurt.magicaljewelry.recipes;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class JewelUpgradeRecipe extends SmithingRecipe implements IRecipe<IInventory>
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
    public boolean matches(IInventory inv, World worldIn)
    {
        ItemStack stackIn = inv.getStackInSlot(0);
        Boolean legendaryCheck = JewelItem.getJewelRarity(stackIn).equals(JewelRarity.LEGENDARY.getName());
        return this.base.test(stackIn) && this.addition.test(inv.getStackInSlot(1)) && !legendaryCheck;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack getCraftingResult(IInventory inv)
    {
        ItemStack itemstack = this.result.copy();
        ItemStack stackIn = inv.getStackInSlot(0);
        CompoundNBT compoundnbt = stackIn.getTag();
        String rarity = JewelItem.getJewelRarity(stackIn);
        if(compoundnbt != null)
        {
            itemstack.setTag(compoundnbt.copy());
            if(rarity.equals(JewelRarity.UNCOMMON.getName()))
            {
                JewelItem.setJewelRarity(itemstack, JewelRarity.RARE.getName());
            }
            else if(rarity.equals(JewelRarity.RARE.getName()))
            {
                JewelItem.setJewelRarity(itemstack, JewelRarity.EPIC.getName());
            }
            else if(rarity.equals(JewelRarity.EPIC.getName()))
            {
                JewelItem.setJewelRarity(itemstack, JewelRarity.LEGENDARY.getName());
            }
        }

        return itemstack;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    @Override
    public ItemStack getRecipeOutput()
    {
        return this.result;
    }

    @Override
    public ItemStack getIcon()
    {
        return new ItemStack(Blocks.SMITHING_TABLE);
    }

    @Override
    public ResourceLocation getId()
    {
        return this.recipeId;
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return Serializer.SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType()
    {
        return IRecipeType.SMITHING;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<JewelUpgradeRecipe>
    {
        public static final Serializer SERIALIZER = new Serializer();

        static
        {
            SERIALIZER.setRegistryName(MagicalJewelry.getId("jewel_upgrade"));
        }

        public JewelUpgradeRecipe read(ResourceLocation recipeId, JsonObject json)
        {
            Ingredient ingredient = Ingredient.deserialize(JSONUtils.getJsonObject(json, "base"));
            Ingredient ingredient1 = Ingredient.deserialize(JSONUtils.getJsonObject(json, "addition"));
            ItemStack itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new JewelUpgradeRecipe(recipeId, ingredient, ingredient1, itemstack);
        }

        public JewelUpgradeRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
        {
            Ingredient ingredient = Ingredient.read(buffer);
            Ingredient ingredient1 = Ingredient.read(buffer);
            ItemStack itemstack = buffer.readItemStack();
            return new JewelUpgradeRecipe(recipeId, ingredient, ingredient1, itemstack);
        }

        public void write(PacketBuffer buffer, JewelUpgradeRecipe recipe)
        {
            recipe.base.write(buffer);
            recipe.addition.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}