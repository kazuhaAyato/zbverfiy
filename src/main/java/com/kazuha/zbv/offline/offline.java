package com.kazuha.zbv.offline;

import com.kazuha.zbv.main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

import static com.kazuha.zbv.main.*;

public class offline implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {
        if(arg.length < 1){
            sendhelp((Player) sender);
            return false;
        }
        switch (arg[0]){
            case "start":{
                if(sender == Bukkit.getConsoleSender()){
                    sender.sendMessage("§c你妈");
                    return true;
                }
                BukkitRunnable t = new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            Connection connection = DriverManager.getConnection(jdbc_url, config.getString("mysql.username"),config.getString("mysql.password"));
                            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `zbcheck` WHERE uuid=?");
                            statement.setString(1,sender.getName());
                            ResultSet set = statement.executeQuery();
                            if(!set.next()){
                                PreparedStatement addnew = connection.prepareStatement("INSERT INTO `zbcheck`(`uuid`, `verified`,`locked`, `time`) VALUES (?,'0','0','0')");
                                addnew.setString(1,sender.getName());
                                addnew.executeUpdate();
                                Bukkit.getPlayer(sender.getName()).kickPlayer(config.getString("messages.start-verify-kick"));
                                addnew.close();
                            }else{
                                if(set.getBoolean("verified")){
                                    sender.sendMessage("§c你已经验证过了！");
                                    dispatcher((Player) sender);
                                }else{
                                    Bukkit.getPlayer(sender.getName()).kickPlayer(config.getString("messages.start-verify-kick"));
                                }
                            }
                            statement.close();
                            connection.close();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                };
                t.runTaskLater(instance,1L);
                return true;
            }
            case "reload":{
                if(sender.hasPermission("zb.all")){
                    config = instance.getConfig();
                    sender.sendMessage("§a成功重载");
                }else{
                    sender.sendMessage("§c缺少权限");
                }
                return true;
            }
        }
        return false;
    }
    public static void dispatcher(Player p){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),config.getString("command_success").replace("%player%",p.getName()));
    }
    //BUG反馈/提交建议 -> §nKazuhaAyato@qq.com
    public void sendhelp(Player p){
        p.sendMessage("§f=========================================");
        p.sendMessage("§f");
        p.sendMessage("§3zb§bverify §f- v"+ main.instance.getDescription().getVersion() + "§7By §3Kazuha§bAyato");
        p.sendMessage("§7§o浮世景色，百千年依旧，人之在世，却如白鹿语泡影，虚无。");
        p.sendMessage("§f");
        p.sendMessage("§b/zb start §f- 开始验证");
        if(p.hasPermission("zb.all")){
            p.sendMessage("§b/zb reload §f- 重载配置文件");
        }
        p.sendMessage("§f");
        p.sendMessage("§f=========================================");
    }

}
