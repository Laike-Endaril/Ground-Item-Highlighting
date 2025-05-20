package com.fantasticsource.grounditemhighlighting;

import com.fantasticsource.mctools.component.path.CPathFollowEntity;
import com.fantasticsource.mctools.items.ItemFilter;
import com.fantasticsource.mctools.particles.PathedParticle;
import com.fantasticsource.mctools.particles.PathedParticleSharedRenderData;
import com.fantasticsource.tools.SpriteMetaData;
import com.fantasticsource.tools.component.path.CPath;
import com.fantasticsource.tools.component.path.CPathConstant;
import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.VectorN;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GroundItemHighlighter
{
    protected static final CPath
            PATH_ALPHA = new CPathConstant(0.2),
            PATH_ITEM_CENTER_OFFSET = new CPathConstant(new VectorN(0, 0.5, 0)),
            PATH_BLOCK_ITEM_CENTER_OFFSET = new CPathConstant(new VectorN(0, 0.35, 0)),
            PATH_SCALE3D_X = new CPathConstant(6, 0.5, 0.5),
            PATH_SCALE3D_Y = new CPathConstant(0.5, 10, 0.5),
            PATH_SCALE3D_Z = new CPathConstant(0.5, 0.5, 6);


    public static PathedParticleSharedRenderData renderData = null;
    public static SpriteMetaData spriteMetaData = null;


    public static ArrayList<ItemFilter> filters = new ArrayList<>();

    public static void sync()
    {
        filters.clear();
        for (String filterString : GroundItemHighlightingConfig.filter)
        {
            ItemFilter filter = ItemFilter.getInstance(filterString);
            if (filter != null) filters.add(filter);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START) return;

        World world = Minecraft.getMinecraft().world;

        if (world == null) return;


        for (EntityItem item : world.getEntities(EntityItem.class, o -> true))
        {
            if (matchesFilters(item) == GroundItemHighlightingConfig.whitelist)
            {
                item.setGlowing(GroundItemHighlightingConfig.glow);

                if (GroundItemHighlightingConfig.particles && !Minecraft.getMinecraft().isGamePaused())
                {
                    CPath path = new CPathFollowEntity(item).add(item.getItem().getItem() instanceof ItemBlock ? PATH_BLOCK_ITEM_CENTER_OFFSET : PATH_ITEM_CENTER_OFFSET);

                    if (renderData == null)
                    {
                        renderData = new PathedParticleSharedRenderData(false, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, "minecraft:textures/particle/particles.png");
                        spriteMetaData = new SpriteMetaData(128, 128, 32, 16, 64, 48);
                    }

                    PathedParticle[] particles = new PathedParticle[3];
                    PathedParticle particle = new PathedParticle(40, renderData);
                    particle.positionPath(path);
                    particle.scale3DPath(new CPathConstant(1, GroundItemHighlightingConfig.particleSize, 1).mult(PATH_SCALE3D_Y));
                    particles[0] = particle;

                    particle = new PathedParticle(40, renderData);
                    particle.positionPath(path);
                    particle.scale3DPath(new CPathConstant(GroundItemHighlightingConfig.particleSize, 1, 1).mult(PATH_SCALE3D_X));
                    particles[1] = particle;

                    particle = new PathedParticle(40, renderData);
                    particle.positionPath(path);
                    particle.scale3DPath(new CPathConstant(1, 1, GroundItemHighlightingConfig.particleSize).mult(PATH_SCALE3D_Z));
                    particles[2] = particle;

                    for (PathedParticle particle2 : particles)
                    {
                        particle2.spriteMetaData = spriteMetaData;
                        particle2.alphaPath(PATH_ALPHA);
                    }

                    if (GroundItemHighlighting.compatTiamatItems)
                    {
                        Color c = com.fantasticsource.grounditemhighlighting.CompatTiamatItems.getItemRarityColor(item.getItem()).copy().setVF(0.7f);
                        for (PathedParticle particle2 : particles)
                        {
                            particle2.rgbPath(new CPathConstant(c.rf(), c.gf(), c.bf()));
                        }
                    }
                }
            }
            else item.setGlowing(false);
        }
    }

    protected static boolean matchesFilters(EntityItem item)
    {
        for (ItemFilter filter : filters)
        {
            if (filter.matches(item.getItem())) return true;
        }

        return false;
    }
}
