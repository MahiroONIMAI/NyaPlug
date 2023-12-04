package cn.miaonai.NyaCatPlugins.Util;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.miaonai.NyaCatPlugins.NyaCatPlugins.VALID_API_KEY;

public class API extends HttpServlet {
    private final Plugin plugin;

    public API(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求路径
        String pathInfo = request.getPathInfo();

        // 判断请求路径是否为 /online
        if ("/api/v1/online".equals(pathInfo)) {
            // 获取请求中的密钥参数
            String apiKey = request.getParameter("key");

            // 验证密钥
            if (isValidApiKey(apiKey)) {
                // 获取BungeeCord总在线玩家数
                int online = online();

                // 构建JSON响应
                String jsonResponse = "{\"online\": " + online + "}";

                // 设置响应类型和状态
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);

                // 返回JSON响应
                response.getWriter().println(jsonResponse);
            } else {
                // 密钥不合法，返回JSON数据
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{\"error\": \"Invalid API key\"}");
            }
        } else {
            // 未知路径，返回404错误
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        if ("/api/v1/player".equals(pathInfo)){//获取玩家信息

            String apiKey = request.getParameter("key");
            String playerUUID = request.getParameter("uuid");

            if (isValidApiKey(apiKey)) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);


            } else {
                // 密钥不合法，返回JSON数据
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("{\"error\": \"Invalid API key\"}");
            }
        }
    }

    private int online() {
        // 获取BungeeCord总在线玩家数的逻辑
        return plugin.getProxy().getOnlineCount();
    }


    private boolean isValidApiKey(String apiKey) {
        return apiKey != null && apiKey.equals(VALID_API_KEY);
    }
}
