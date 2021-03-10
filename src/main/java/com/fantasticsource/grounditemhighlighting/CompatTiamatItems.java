package com.fantasticsource.grounditemhighlighting;

import com.fantasticsource.tiamatitems.nbt.MiscTags;
import com.fantasticsource.tiamatitems.settings.CRarity;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.item.ItemStack;

public class CompatTiamatItems
{
    public static Color getItemRarityColor(ItemStack stack)
    {
        CRarity rarity = MiscTags.getItemRarity(stack);
        if (rarity == null) return Color.WHITE;
        else return rarity.color;
    }
}
