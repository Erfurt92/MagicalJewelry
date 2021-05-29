package com.erfurt.magicaljewelry.util.interfaces;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelAttributes
{
    Random rand = new Random();

    List<Attribute> attributesList = new ArrayList<>();
    List<String> descriptionAttributesList = new ArrayList<>();

    static void init()
    {
        Attributes();
    }

    static void Attributes()
    {
        addNewAttribute(0, Attributes.ARMOR_TOUGHNESS, "Armor Toughness bonus");
        addNewAttribute(1, Attributes.ARMOR, "Armor bonus");
        addNewAttribute(2, Attributes.ATTACK_DAMAGE, "Attack Damage bonus");
        addNewAttribute(3, Attributes.MAX_HEALTH, "Health Boost bonus");
        addNewAttribute(4, Attributes.KNOCKBACK_RESISTANCE, "Knockback Resistance bonus");
    }

    static void addNewAttribute(int index, Attribute attribute, String description)
    {
        attributesList.add(index, attribute);
        descriptionAttributesList.add(index, description);
    }

    static int getAttributes()
    {
        return rand.nextInt(attributesList.size());
    }
}