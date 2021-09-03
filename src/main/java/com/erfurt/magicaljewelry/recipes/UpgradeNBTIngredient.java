package com.erfurt.magicaljewelry.recipes;

import com.erfurt.magicaljewelry.objects.items.JewelItem;
import com.erfurt.magicaljewelry.util.enums.JewelRarity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

public class UpgradeNBTIngredient extends Ingredient
{
    private final ItemStack stack;
    protected UpgradeNBTIngredient(ItemStack stack)
    {
        super(Stream.of(new Ingredient.ItemValue(stack)));
        this.stack = stack;
    }

    @Override
    public boolean test(@Nullable ItemStack input)
    {
        JsonObject json = toJson().getAsJsonObject();
        boolean rarityCheck = false;
        if(input != null && input.hasTag() && input.getItem() instanceof JewelItem)
        {
            boolean rarityCheckTemp = JewelRarity.containsRarity(JewelItem.getJewelRarity(input));
            if(rarityCheckTemp)
            {
                String rarityTemp = json.get("nbt").getAsString();
                int stringSize = rarityTemp.length();
                String rarity = rarityTemp.substring(1, stringSize - 1);
                rarityCheck = Objects.requireNonNull(input.getTag().get(JewelItem.NBT_RARITY)).getAsString().equals(rarity);
            }
        }
        return this.stack.getItem() == input.getItem() && rarityCheck;
    }

    @Override
    public boolean isSimple()
    {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(UpgradeNBTIngredient.Serializer.INSTANCE).toString());
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        if(stack.hasTag()) json.addProperty("nbt", stack.getTag().get(JewelItem.NBT_RARITY).toString());
        return json;
    }

    public static class Serializer implements IIngredientSerializer<UpgradeNBTIngredient>
    {
        public static final UpgradeNBTIngredient.Serializer INSTANCE = new UpgradeNBTIngredient.Serializer();

        @Override
        public UpgradeNBTIngredient parse(FriendlyByteBuf buffer)
        {
            return new UpgradeNBTIngredient(buffer.readItem());
        }

        @Override
        public UpgradeNBTIngredient parse(JsonObject json)
        {
            return new UpgradeNBTIngredient(CraftingHelper.getItemStack(json, true));
        }

        @Override
        public void write(FriendlyByteBuf buffer, UpgradeNBTIngredient ingredient)
        {
            buffer.writeItem(ingredient.stack);
        }
    }
}