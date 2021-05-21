package com.erfurt.magicaljewelry.data.client;

import com.erfurt.magicaljewelry.MagicalJewelry;
import com.erfurt.magicaljewelry.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider
{
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, MagicalJewelry.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        jewelItem(itemGenerated, ItemInit.GOLD_AMULET.get().toString(), "amulet");
        jewelItem(itemGenerated, ItemInit.SILVER_AMULET.get().toString(), "amulet");
        jewelItem(itemGenerated, ItemInit.GOLD_RING.get().toString(), "ring");
        jewelItem(itemGenerated, ItemInit.SILVER_RING.get().toString(), "ring");
        jewelItem(itemGenerated, ItemInit.GOLD_BRACELET.get().toString(), "bracelet");
        jewelItem(itemGenerated, ItemInit.SILVER_BRACELET.get().toString(), "bracelet");

        getBuilder(ItemInit.THE_ONE_RING.get().toString()).parent(itemGenerated).texture("layer0", "item/gold_ring");
    }

    private ItemModelBuilder jewelItem(ModelFile itemGenerated, String name, String jewelType)
    {
        return getBuilder(name).parent(itemGenerated).texture("layer0", "item/gem_" + jewelType).texture("layer1", "item/" + name);
    }
}