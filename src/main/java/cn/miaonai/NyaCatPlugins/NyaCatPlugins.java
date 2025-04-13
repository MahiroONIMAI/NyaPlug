package cn.miaonai.NyaCatPlugins;

import cn.miaonai.NyaCatPlugins.Command.BindAccount;
import cn.miaonai.NyaCatPlugins.Command.FastLogin;
import cn.miaonai.NyaCatPlugins.Command.UnBindAccount;
import cn.miaonai.NyaCatPlugins.Util.InsecureWebSocketClient;
import cn.miaonai.NyaCatPlugins.Util.SSLUtil;
import com.alibaba.fastjson2.JSONObject;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public  class NyaCatPlugins extends Plugin implements Listener {

    private static InsecureWebSocketClient wsClient;

    public static int port;
    public static boolean ssl,ignorecerterrors;
    public static String host,serverid,serverkey;


    @Override
    public void onEnable() {

        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File configFile = new File(getDataFolder(), "config.yml");

            Configuration config;
            if (!configFile.exists()) {
                // 如果没有他妈的配置文件，则保存他妈的默认配置
                configFile.createNewFile();
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
                // 设置他妈的默认值
                config.set("host", "localhost");
                config.set("port", 8080);
                config.set("ssl",true);
                config.set("ignore-cert-errors",true);
                config.set("serverid", "15772");
                config.set("serverkey", "5as4564t21sd234f54qwr4564dfsg");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } else {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            }

            // 检查配置字段是否他妈的存在并读取他妈的喵！
            if (config.contains("host") && config.contains("port") &&
                    config.contains("serverid") && config.contains("serverkey") && config.contains("ssl") && config.contains("ignore-cert-errors")) {

                String ihost = config.getString("host");
                int iport = config.getInt("port");
                String iserverid = config.getString("serverid");
                String iserverkey = config.getString("serverkey");
                boolean issl= config.getBoolean("ssl");
                boolean iignorecerterrors = config.getBoolean("ignore-cert-errors");

                host = ihost;
                serverid = iserverid;
                serverkey = iserverkey;
                port = iport;
                ssl = issl;
                ignorecerterrors = iignorecerterrors;

            } else {//返回傻逼报错喵！
                getLogger().warning("配置文件缺少必要的字段！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }//声明作者你爹爹的信息喵！
        getLogger().info("NyanServerExtension已加载 , Developer: ItzHoshinoDev_");
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new BindAccount(this));
        getProxy().getPluginManager().registerCommand(this,new UnBindAccount());
        getProxy().getPluginManager().registerCommand(this,new FastLogin());
        connectWebSocket();
    }
    private void connectWebSocket() {
        try {
            String IsSSL = "";
            if (ssl){
                IsSSL = "wss://";
            }else {
                IsSSL = "ws://";
            }
            SSLContext sslContext = SSLUtil.createIgnoreSSLContext();
            wsClient = new InsecureWebSocketClient(new URI(IsSSL+host+":"+port+"/api/zako/v3/websocket/bungee/"+serverid+"/"+serverkey)) {
                @Override
                protected void onSetSSLParameters(SSLParameters sslParameters) {
                    sslParameters.setEndpointIdentificationAlgorithm("");
                }


                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    getLogger().info("已与NyanID建立Websocket连接");
                }

                @Override
                public void onMessage(String message) {
                    JSONObject json = JSONObject.parseObject(message);
                    String packet = json.getString("packet");
                    switch (packet) {
                        case "S01"://完成账户绑定
                            UUID uuid = UUID.fromString(json.getString("uuid"));
                            ProxiedPlayer player = getProxy().getPlayer(uuid);
                            player.sendMessage("§5§l小鳥遊ホシノ §b§l»§l§6The binding is successful, and the UID of the bound NyanUser is §c§l" + json.getString("nuid"));
                            break;
                        case "S02"://全局广播
                            String type = json.getString("type");
                            String msg = json.getString("msg");
                            boolean havelink = json.getBoolean("havalink");
                            String link = json.getString("link");
                            TextComponent textComponent = new TextComponent();
                            textComponent.setText("[System Message-"+type+"]§5§l小鳥遊ホシノ §b§l»§l§c"+msg);
                            textComponent.setBold(true);
                            if (havelink){
                                textComponent.setClickEvent(new ClickEvent( ClickEvent.Action.OPEN_URL, link ));
                            }
                            getProxy().broadcast();
                            break;
                        case "S32"://checkbind
                            boolean Isbind = json.getBoolean("bind");
                            if (Isbind){
                                UUID uid = UUID.fromString(json.getString("muid"));
                                String nuid = json.getString("uuid");
                                String username = json.getString("username");
                                ProxiedPlayer p = getProxy().getPlayer(uid);
                                p.sendMessage("§5§l小鳥遊ホシノ §b§l»§l§6此Minecraft账户已绑定用户名为 §c§l»"+username+"["+nuid+"]"+"§l§6的NyanID用户" );
                            } else {
                                BindAccount.sendMessage();
                            }
                            break;
                        default:
                            getLogger().info(message);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    getLogger().warning("WebSocket连接关闭: " + reason);
                    scheduleReconnect();
                }

                @Override
                public void onError(Exception ex) {
                    getLogger().severe("WebSocket错误: " + ex.getMessage());
                }
            };
            if (ignorecerterrors) {
                SSLParameters sslParams = sslContext.getSupportedSSLParameters();
                sslParams.setEndpointIdentificationAlgorithm("");
                wsClient.setSocketFactory(sslContext.getSocketFactory());
                wsClient.setSSLParameters(sslParams);

            }

            // 设置连接超时
            wsClient.setConnectionLostTimeout(60);
            wsClient.connectBlocking(5, TimeUnit.SECONDS);

            wsClient.connect();
        } catch (URISyntaxException e) {
            getLogger().severe("WebSocket URL格式错误: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendWebSocketMessage(String message) {
        if (wsClient != null && wsClient.isOpen()) {
            wsClient.send(message);
        }
    }

    private void scheduleReconnect() {
        getProxy().getScheduler().schedule(this, () -> {
            getLogger().info("尝试重新连接WebSocket...");
            connectWebSocket();
        }, 10, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        wsClient.close(10001);
        getLogger().info("NyanServerExtension已卸载 , Developer: ItzHoshinoDev_");
        }

}