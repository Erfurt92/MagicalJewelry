package com.erfurt.magicaljewelry.util.interfaces;

import net.minecraft.entity.SharedMonsterAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface IJewelAttributes
{
    Random rand = new Random();

    List<String> nameAttributesList = new ArrayList<>();
    List<String> descriptionAttributesList = new ArrayList<>();

    static void init()
    {
        Attributes();
    }

    static void Attributes()
    {
        addNewAttribute(0, SharedMonsterAttributes.ARMOR_TOUGHNESS.getName(), "Armor Toughness bonus");
        addNewAttribute(1, SharedMonsterAttributes.ARMOR.getName(), "Armor bonus");
        addNewAttribute(2, SharedMonsterAttributes.ATTACK_DAMAGE.getName(), "Attack Damage bonus");
        addNewAttribute(3, SharedMonsterAttributes.MAX_HEALTH.getName(), "Health Boost bonus");
        addNewAttribute(4, SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), "Knockback Resistance bonus");
    }

    static void addNewAttribute(int index, String name, String description)
    {
        nameAttributesList.add(index, name);
        descriptionAttributesList.add(index, description);
    }

    static int getAttributes()
    {
        return rand.nextInt(nameAttributesList.size());
    }
}