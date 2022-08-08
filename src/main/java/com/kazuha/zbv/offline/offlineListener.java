package com.kazuha.zbv.offline;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import static com.kazuha.zbv.main.*;
import static com.kazuha.zbv.main.jdbc_url;

public class offlineListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        BukkitRunnable t = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection connection = DriverManager.getConnection(jdbc_url, config.getString("mysql.username"),config.getString("mysql.password"));
                    PreparedStatement statements = connection.prepareStatement("SELECT * FROM `zbcheck` WHERE uuid=?");
                    statements.setString(1,e.getPlayer().getName());
                    ResultSet set = statements.executeQuery();
                    if(!set.next()){
                        return;
                    }else{
                        if(set.getBoolean("locked")){
                            statements.close();
                            connection.close();
                            return;
                        }
                        if(set.getBoolean("verified")){
                            offline.dispatcher(e.getPlayer());
                            e.getPlayer().sendMessage(config.getString("messages.verify-success"));
                            PreparedStatement statement = connection.prepareStatement("UPDATE `zbcheck` SET `locked`='1' WHERE `uuid`=?");
                            statement.setString(1, e.getPlayer().getName());
                            statement.executeUpdate();
                            statement.close();
                            statements.close();
                            connection.close();
                            return;
                        }
                    }
                }catch (SQLException c){
                    c.printStackTrace();
                }
            }
        };
        t.runTaskAsynchronously(instance);
    }
}
