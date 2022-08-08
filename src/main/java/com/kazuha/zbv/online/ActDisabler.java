package com.kazuha.zbv.online;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
public class ActDisabler implements Listener {
    @EventHandler
    public void oninteract(PlayerInteractEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onchat(PlayerChatEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onmove(PlayerMoveEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void ondmg(EntityDamageEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onTP(PlayerTeleportEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void noInventory(InventoryOpenEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void onKill(PlayerPickupItemEvent e){
        e.setCancelled(true);
    }
}
