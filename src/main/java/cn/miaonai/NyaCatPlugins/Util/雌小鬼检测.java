package cn.miaonai.NyaCatPlugins.Util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class 雌小鬼检测 {

    public static String getSkinType(UUID uuid) {//审判你的性别喵！
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", ""));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();
                reader.close();

                String base64EncodedString = response.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
                String decodedString = new String(Base64.getDecoder().decode(base64EncodedString));
                JsonObject textures = JsonParser.parseString(decodedString).getAsJsonObject().getAsJsonObject("textures");

                if (textures != null && textures.has("SKIN")) {
                    JsonObject skin = textures.getAsJsonObject("SKIN");
                    if (skin.has("metadata") && "slim".equals(skin.getAsJsonObject("metadata").get("model").getAsString())) {
                        return "雌小鬼喵";//雌小鬼喵
                    }
                }
                return "杂鱼喵";//雄小鬼喵
            }
        } catch (Exception e) {
            System.out.println("访问MojangAPI遇到错误,请检查API是否能使用喵!");
            e.printStackTrace();

        }
        return "雌小鬼喵";//操你妈的没有性别喵！！你知道为什么吗,因为BugJump没给你安那个玩意，傻逼喵！！！！！！！！！！！！！！！！
    }

}
