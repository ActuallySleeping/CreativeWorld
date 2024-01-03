package net.fabricmc.BuildingDimension.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    /**
     * Prevents entities from leaving any building dimension.
     * For example, throwing something in a nether portal frame.
     *
     * @param destination The destination
     * @param cir Callback info
     */
    @Inject(
            method = "moveToWorld",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMoveToWorld(@NotNull ServerWorld destination, CallbackInfoReturnable<Entity> cir){
        Entity entity = (Entity)(Object)this;

        RegistryKey<World> target_dim = destination.getRegistryKey();
        RegistryKey<World> source_dim = entity.getWorld().getRegistryKey();

        if (! target_dim.getValue().getNamespace().equals("building_dimension") &&
                source_dim.getValue().getNamespace().equals("building_dimension")) {

            cir.setReturnValue(entity);
        }
    }
}
