package cn.miaonai.NyaCatPlugins.Util;

import java.util.Random;

public class RandomEv {
    public static String RandomID() {//给你妈的随机生成ID喵！
        String characters = "abdghilnoqrsuvwy0123456789";
        StringBuilder id = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
    }
    public static String RandomLoginToken() {//生成你妈的随机登录Token喵！
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder flt = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 35; i++) {
            int index = random.nextInt(characters.length());
            flt.append(characters.charAt(index));
        }

        return flt.toString();
    }
    public static String generateRandomToken() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            token.append(characters.charAt(index));
        }

        return token.toString();
    }
}
