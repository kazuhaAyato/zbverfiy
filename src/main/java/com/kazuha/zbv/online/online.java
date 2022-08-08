package com.kazuha.zbv.online;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import static com.kazuha.zbv.main.*;
/*
stats:
0 - NOT STARTED
1 - IN PROCESS
2 - SUCCESSED
3 - LOCKED
 */
import java.sql.*;


public class online implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLoginEvent(PlayerJoinEvent e){
            BukkitRunnable t = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Connection connection = DriverManager.getConnection(jdbc_url, config.getString("mysql.username"),config.getString("mysql.password"));
                        PreparedStatement statements = connection.prepareStatement("SELECT * FROM `zbcheck` WHERE uuid=?");
                        statements.setString(1,e.getPlayer().getName());
                        ResultSet set = statements.executeQuery();
                        if(set.next()) {
                            if(set.getBoolean("verified")){
                                e.getPlayer().kickPlayer(config.getString("messages.already-verified"));
                                return;
                            }
                            PreparedStatement statement = connection.prepareStatement("UPDATE `zbcheck` SET `verified`='1',`time`=? WHERE `uuid`=?");
                            statement.setLong(1, System.currentTimeMillis());
                            statement.setString(2, e.getPlayer().getName());
                            statement.executeUpdate();
                            statement.close();
                            e.getPlayer().kickPlayer(config.getString("messages.verfiy-title"));
                        }else {
                            e.getPlayer().kickPlayer(config.getString("messages.not-exist"));
                            return;
                        }
                        statements.close();
                        connection.close();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }

                }
            };
        t.runTaskLater(instance,1L);
        }
}
