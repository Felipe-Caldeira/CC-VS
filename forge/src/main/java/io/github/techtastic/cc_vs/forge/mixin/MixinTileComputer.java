package io.github.techtastic.cc_vs.forge.mixin;

import dan200.computercraft.shared.computer.blocks.TileComputer;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import io.github.techtastic.cc_vs.apis.ExtendedShipAPI;
import io.github.techtastic.cc_vs.apis.ShipAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

@Mixin(TileComputer.class)
public class MixinTileComputer {
    @Inject(
            method = "createComputer",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void cc_vs$addShipAPI(int id, CallbackInfoReturnable<ServerComputer> cir) {
        ServerComputer computer = cir.getReturnValue();
        ServerLevel level = computer.getLevel();
        BlockPos pos = computer.getPosition();
        ServerShip ship = VSGameUtilsKt.getShipManagingPos(level, pos);

        if (ship != null)
            if (computer.getFamily().equals(ComputerFamily.COMMAND))
                computer.addAPI(new ExtendedShipAPI(computer.getAPIEnvironment(), ship));
            else
                computer.addAPI(new ShipAPI(computer.getAPIEnvironment(), ship));

        cir.setReturnValue(computer);
    }
}
