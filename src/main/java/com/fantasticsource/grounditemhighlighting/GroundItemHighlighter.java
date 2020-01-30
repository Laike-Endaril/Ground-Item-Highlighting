package com.fantasticsource.grounditemhighlighting;

import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.items.ItemFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;

@SideOnly(Side.CLIENT)
public class GroundItemHighlighter
{
    public static ArrayList<ItemFilter> filters = new ArrayList<>();
    public static HashSet<EntityItem> groundItems = new HashSet<>(), filteredGroundItems = new HashSet<>();

    public static void sync()
    {
        filters.clear();
        for (String filterString : GroundItemHighlightingConfig.filter)
        {
            ItemFilter filter = ItemFilter.getInstance(filterString);
            if (filter != null) filters.add(filter);
        }

        filteredGroundItems.clear();
        for (EntityItem item : groundItems) onItemJoin(item);
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof EntityItem)) return;

        EntityItem item = (EntityItem) entity;
        groundItems.add(item);

        onItemJoin(item);
    }

    protected static void onItemJoin(EntityItem item)
    {
        if (item.getItem().getItem() == Items.AIR)
        {
            ClientTickTimer.schedule(1, () -> onItemJoin(item));
            return;
        }

        boolean matchesFilter = false;
        for (ItemFilter filter : filters)
        {
            if (filter.matches(item.getItem()))
            {
                matchesFilter = true;
                break;
            }
        }

        if (GroundItemHighlightingConfig.whitelist)
        {
            if (matchesFilter) filteredGroundItems.add(item);
        }
        else
        {
            if (!matchesFilter) filteredGroundItems.add(item);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) return;

        World world = Minecraft.getMinecraft().world;

        if (world == null)
        {
            groundItems.clear();
            filteredGroundItems.clear();
            return;
        }


        for (EntityItem item : groundItems.toArray(new EntityItem[0]))
        {
            if (!world.loadedEntityList.contains(item) || item.world != world)
            {
                item.setGlowing(false);
                groundItems.remove(item);
                filteredGroundItems.remove(item);
                continue;
            }

            if (filteredGroundItems.contains(item))
            {
                item.setGlowing(GroundItemHighlightingConfig.glow);

                if (GroundItemHighlightingConfig.particles && !Minecraft.getMinecraft().isGamePaused() && ClientTickTimer.currentTick() % 5 == 0)
                {
                    world.spawnParticle(EnumParticleTypes.END_ROD, item.posX, item.posY, item.posZ, (-0.5 + Math.random()) * 0.1, 0.25, (-0.5 + Math.random()) * 0.1);
                }
            }
            else item.setGlowing(false);
        }
    }
}
