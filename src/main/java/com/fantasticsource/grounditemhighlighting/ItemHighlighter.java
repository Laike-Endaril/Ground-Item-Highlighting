package com.fantasticsource.grounditemhighlighting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemHighlighter
{
    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityItem)) return;

        EntityItem item = (EntityItem) entity;
        item.setGlowing(true);
    }
}
