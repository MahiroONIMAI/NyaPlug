package cn.miaonai.NyaCatPlugins.Account;

import cn.miaonai.NyaCatPlugins.NyaCatPlugins;
import cn.miaonai.NyaCatPlugins.Util.RandomEv;
import cn.miaonai.NyaCatPlugins.Util.雌小鬼检测;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import static cn.miaonai.NyaCatPlugins.Util.NyaSQL.*;

public class Account extends NyaCatPlugins implements Listener {
    public static class GetTokenCommand extends Command {
        public GetTokenCommand() {
            super("gettoken");
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage("Only players can use this command.");
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;

            try {
                openConnection();
                if (connection != null && !connection.isClosed()){
                    // 检查数据库中是否已经存在该玩家的令牌
                    PreparedStatement checkStatement = connection.prepareStatement("SELECT token FROM player_tokens WHERE uuid = ?");
                    checkStatement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = checkStatement.executeQuery();
                    String hg;
                    if(!雌小鬼检测.getSkinType(player.getUniqueId())) {
                        hg = "雌小鬼";
                    }else hg = "杂鱼哥哥";
                    if (resultSet.next()) {
                        String token = resultSet.getString("token");
                        player.sendMessage("§5§l緒山まひろ §b§l»§l§6你已经获取令牌了哦喵~,"+hg+": " + token);
                    } else {

                        String token = RandomEv.generateRandomToken();

                        // 插入新的令牌到数据库
                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO player_tokens (uuid, token) VALUES (?, ?)");
                        insertStatement.setString(1, player.getUniqueId().toString());
                        insertStatement.setString(2, token);
                        insertStatement.executeUpdate();
                        player.sendMessage("§5§l緒山まひろ §b§l»§4你的令牌是: " + token + "喵~");
                        connection.close();
                    }
                }
            } catch (SQLException | ClassNotFoundException e) {
                player.sendMessage("§5§l緒山まひろ §b§l»发生错误" );
                e.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class RegisterAccount extends Command {
        public RegisterAccount() {
            super("reg");
        }

        @Override
        public void execute(CommandSender commandSender, String[] args) {
            if (!(commandSender instanceof ProxiedPlayer)) {
                commandSender.sendMessage("Only players can use this command.");
            }
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            // 确保玩家输入了两个参数
            if (args.length != 2) {
                commandSender.sendMessage("§5§l緒山まひろ §b§l»§4使用方法: /reg [邮箱] [密码]");
            } else {

                String email = args[0];
                String password = args[1];

                // 验证邮箱格式
                if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
                    commandSender.sendMessage("§5§l緒山まひろ §b§l»§4请输入有效的邮箱地址喵~");
                }
                if (password.length() < 6) {
                    commandSender.sendMessage("§5§l緒山まひろ §b§l»§4密码长度至少为6位喵~");
                }


                try {
                    openConnection();
                    // 检查数据库中是否已经存在该玩家的账户
                    PreparedStatement checkStatement = connection.prepareStatement("SELECT id FROM accounts WHERE mcuuid = ?");
                    checkStatement.setString(1, player.getUniqueId().toString());
                    ResultSet resultSet = checkStatement.executeQuery();
                    String hg;
                    if (!雌小鬼检测.getSkinType(player.getUniqueId())) {
                        hg = "雌小鬼";
                    } else hg = "杂鱼哥哥";

                    if (resultSet.next()) {
                        String id = resultSet.getString("id");
                        player.sendMessage("§5§l緒山まひろ §b§l»§l§6你已经注册了哦喵~," + hg + "你的NyaID: " + id);
                    } else {
                        // 创建一个用于HMAC SHA-256的Mac实例
                        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                        // 使用密钥初始化Mac对象
                        SecretKeySpec secret_key = new SecretKeySpec(encryptionKey.getBytes(), "HmacSHA256");
                        sha256_HMAC.init(secret_key);
                        // 执行HMAC加密
                        byte[] hash = sha256_HMAC.doFinal(password.getBytes());
                        String lockpwd = Base64.getEncoder().encodeToString(hash);
                        String UID = RandomEv.RandomID();
                        // 插入新的令牌到数据库
                        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO accounts (id, mcuuid,username,password,email,Permissions,level,token,Bcookie) VALUES (?, ?, ?, ?, ?, ?, ?, ? ,?)");
                        insertStatement.setString(1, UID);
                        insertStatement.setString(2, player.getUniqueId().toString());
                        insertStatement.setString(3, player.getName());
                        insertStatement.setString(4, lockpwd);
                        insertStatement.setString(5, email);
                        insertStatement.setString(6, "1");
                        insertStatement.setString(7, "0");
                        insertStatement.setString(8, "NULL");
                        insertStatement.setString(9, "NULL");
                        insertStatement.executeUpdate();
                        player.sendMessage("§5§l緒山まひろ §b§l»§6注册成功喵ヾ(≧▽≦*)o," + hg + "您的NyaID: " + UID);
                    }
                } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static class FastLogin extends Command{
        public FastLogin(){super("fastlogin");}

        @Override
        public void execute(CommandSender commandSender, String[] strings) {
            if (!(commandSender instanceof ProxiedPlayer)) {
                commandSender.sendMessage("Only players can use this command.");

            }
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            try {
                openConnection();
                // 检查数据库中是否已经存在该玩家的账户
                PreparedStatement checkStatement = connection.prepareStatement("SELECT id FROM accounts WHERE mcuuid = ?");
                checkStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    try {
                        String hg;
                        if(!雌小鬼检测.getSkinType(player.getUniqueId())) {
                            hg = "雌小鬼";
                        }else hg = "杂鱼哥哥";
                        String FastLoginToken = RandomEv.RandomLoginToken();
                        PreparedStatement  insertStatement = connection.prepareStatement("UPDATE accounts SET token = ? WHERE mcuuid = ?");
                        insertStatement.setString(1, FastLoginToken);
                        insertStatement.setString(2, player.getUniqueId().toString());
                        insertStatement.executeUpdate();
                        TextComponent message = new TextComponent("§5§l緒山まひろ §b§l»§l§6成功创建快速登录令牌, "+hg+"喵~,点我快速登录!ヾ(≧▽≦*)o");
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.nyacat.cloud/authe/fastlogin/"+FastLoginToken));
                        player.sendMessage(message);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else{
                    String hg;
                    if(!雌小鬼检测.getSkinType(player.getUniqueId())) {
                        hg = "雌小鬼";
                    }else hg = "杂鱼哥哥";
                    commandSender.sendMessage("§5§l緒山まひろ §b§l»§4您还未注册NyaID,"+hg+"喵~请使用指令/reg [邮箱] [密码]注册NyaID");
                    connection.close();
                }


            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}
