package robmart.mods.targetingapi.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import robmart.mods.targetingapi.api.Targeting;

import java.util.function.Predicate;

@Mod.EventBusSubscriber
public class DebugMenuGui extends AbstractGui {

    @SubscribeEvent
    public static void onRenderGameOverlay(final RenderGameOverlayEvent.Text event) {
        if (event.getRight().size() < 1 || Minecraft.getInstance().getRenderViewEntity() == null) return;
        Entity entityIn = Minecraft.getInstance().getRenderViewEntity();
        int distance = 8;

        Vec3d vec3d = entityIn.getEyePosition(1.0F);
        Vec3d vec3d1 = entityIn.getLook(1.0F).scale((double)distance);
        Vec3d vec3d2 = vec3d.add(vec3d1);
        AxisAlignedBB axisalignedbb = entityIn.getBoundingBox().expand(vec3d1).grow(1.0D);
        Predicate<Entity> predicate = (p_217727_0_) -> {
            return !p_217727_0_.isSpectator() && p_217727_0_.canBeCollidedWith();
        };
        EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(entityIn, vec3d, vec3d2, axisalignedbb, predicate, (double)distance * distance);
        if (entityraytraceresult != null) {
            for (int i = 0; i < event.getRight().size(); i++) {
                if (event.getRight().get(i).contains("Targeted Entity")) {
                    if (event.getRight().get(i + 1) != null) {
                        event.getRight().add(i + 2, "Factions:");
                        for (int j = 0; j < Targeting.getFactionsFromEntity(entityraytraceresult.getEntity()).size(); j++) {
                            event.getRight().add(i + 3 + j, Targeting.getFactionsFromEntity(entityraytraceresult.getEntity()).get(j).getName());
                        }
                    } else {
                        event.getRight().add("Factions:");
                        for (int j = 0; j < Targeting.getFactionsFromEntity(entityraytraceresult.getEntity()).size(); j++) {
                            Targeting.getFactionsFromEntity(entityraytraceresult.getEntity()).get(j).getName();
                        }
                    }
                }
            }
        }
    }
}
