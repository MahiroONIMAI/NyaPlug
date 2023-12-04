package cn.miaonai.NyaCatPlugins;

import cn.miaonai.NyaCatPlugins.Account.Account;
import cn.miaonai.NyaCatPlugins.Commands.Commands;
import cn.miaonai.NyaCatPlugins.Util.API;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static cn.miaonai.NyaCatPlugins.Util.NyaSQL.connection;

/*




 */

public  class NyaCatPlugins extends Plugin implements Listener {

    public static int port,stats_port;
    public static String host,database,password,username,encryptionKey;
    public static  String VALID_API_KEY;

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
                config.set("port", 3306);
                config.set("db", "xycloud");
                config.set("uname", "xycloud");
                config.set("pwd", "xycloud");
                config.set("encryption_key", "2d4fg544r413243ync67t43c642cb678xz78324cbnnd81gh27ggrfg823fgsidg1nmz89wqhedruighsa8q09sdnxc8rb9f72m446b21897dvnaskdjh34bmxzcb325vbksahd");
                config.set("stats_key","IAN9sd7b7Dfga78HD89");
                config.set("stats_port",2546);
                config.set("stats_host","127.0.0.1");
                config.set("ZuroNeko_API","http://yourapilink.you/v1");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } else {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            }

            // 检查配置字段是否他妈的存在并读取他妈的喵！
            if (config.contains("host") && config.contains("port") &&
                    config.contains("db") && config.contains("uname") &&
                    config.contains("pwd") && config.contains("encryption_key")&&
                    config.contains("stats_port")&& config.contains("stats_host")&&
                    config.contains("ZuroNeko_API")&&config.contains("stats_key")) {

                String ihost = config.getString("host");
                int iport = config.getInt("port");
                String idatabase = config.getString("db");
                String iusername = config.getString("uname");
                String ipassword = config.getString("pwd");
                String iencryptionKey = config.getString("encryption_key");
                String ApiKey = config.getString("stats_key");
                int Aport = config.getInt("stats_port");

                host = ihost;
                username = iusername;
                database = idatabase;
                password = ipassword;
                encryptionKey = iencryptionKey;
                port = iport;
                stats_port = Aport;
                VALID_API_KEY = ApiKey;

            } else {//返回傻逼报错喵！
                getLogger().warning("配置文件缺少必要的字段！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }//声明作者你爹爹的信息喵！
        System.out.println("NyaCatPlugins已加载 , Developer: MahiroNEKO(星野喵奈1337 , ver2.0)");
        startApiServer();

        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new Account.GetTokenCommand());
        getProxy().getPluginManager().registerCommand(this, new Account.RegisterAccount());
        getProxy().getPluginManager().registerCommand(this, new Account.FastLogin());
        getProxy().getPluginManager().registerCommand(this, new Commands.Help());
        getProxy().getPluginManager().registerCommand(this, new Commands.HidePlugins());
        getProxy().getPluginManager().registerCommand(this, new Commands.HideBungee());

    }
    @Override
    public void onDisable() {


        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                getLogger().info("Disconnected from MySQL database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void startApiServer() {
        try {
            // 创建Jetty服务器
            Server server = new Server(stats_port);

            // 创建Servlet上下文
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            // 将插件实例传递给Servlet
            context.addServlet(new ServletHolder(new API(this)), "/*");

            // 将Servlet上下文添加到服务器
            server.setHandler(context);

            // 启动服务器
            server.start();
            System.out.println("NyaAPI已启用喵~ 127.0.0.1:"+stats_port);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NyaAPI启动遇到问题,请检查端口是否占用");
        }
    }
}