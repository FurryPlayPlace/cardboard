package org.cardboardpowered.impl;

import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;

import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import io.papermc.paper.math.Position;

public class CardboardDragonBattle implements DragonBattle {

    private final EnderDragonFight handle;

    public CardboardDragonBattle(EnderDragonFight handle) {
        this.handle = handle;
    }

    @Override
    public EnderDragon getEnderDragon() {
        return null; // TODO
    }

    @Override
    public BossBar getBossBar() {
        return null; // TODO
    }

    @Override
    public Location getEndPortalLocation() {
        return null; // TODO
    }

    @Override
    public boolean generateEndPortal(boolean withPortals) {
        // TODO
        return true;
    }

    @Override
    public boolean hasBeenPreviouslyKilled() {
        return handle.hasPreviouslyKilled();
    }

    @Override
    public void initiateRespawn() {
        this.handle.respawnDragon();
    }

    @Override
    public RespawnPhase getRespawnPhase() {
        return RespawnPhase.NONE; // TODO
    }

    @Override
    public boolean setRespawnPhase(RespawnPhase phase) {
        // TODO
        return true;
    }

    @Override
    public void resetCrystals() {
        this.handle.resetEndCrystals();
    }

    @Override
    public int hashCode() {
        return handle.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CardboardDragonBattle && ((CardboardDragonBattle) obj).handle == this.handle;
    }

    private RespawnPhase toBukkitRespawnPhase(EnderDragonSpawnState phase) {
        return (phase != null) ? RespawnPhase.values()[phase.ordinal()] : RespawnPhase.NONE;
    }

    private EnderDragonSpawnState toNMSRespawnPhase(RespawnPhase phase) {
        return (phase != RespawnPhase.NONE) ? EnderDragonSpawnState.values()[phase.ordinal()] : null;
    }

	@Override
	public boolean initiateRespawn(@Nullable Collection<EnderCrystal> enderCrystals) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getGatewayCount() {
		// TODO Auto-generated method stub
		//return 20 - this.handle.gateways.size();
		return -1;
	}

	@Override
	public boolean spawnNewGateway() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void spawnNewGateway(@NotNull Position position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public @NotNull @Unmodifiable List<EnderCrystal> getRespawnCrystals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public @NotNull @Unmodifiable List<EnderCrystal> getHealingCrystals() {
		// TODO Auto-generated method stub
		return null;
	}
}
