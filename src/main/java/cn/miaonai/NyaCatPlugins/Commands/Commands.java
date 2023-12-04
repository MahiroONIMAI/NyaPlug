package cn.miaonai.NyaCatPlugins.Commands;

import cn.miaonai.NyaCatPlugins.NyaCatPlugins;
import cn.miaonai.NyaCatPlugins.Util.雌小鬼检测;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class Commands extends NyaCatPlugins implements Listener {
    public static class Help extends Command {
        public Help(){super("help");}

        @Override
        public void execute(CommandSender commandSender, String[] page) {
            String p1 = page[0];
            if (!(commandSender instanceof ProxiedPlayer)) {
                commandSender.sendMessage("Only players can use this command.");
            }
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            String hg;
            if(!雌小鬼检测.getSkinType(player.getUniqueId())) {
                hg = "雌小鬼";
            }else hg = "杂鱼哥哥";
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l绪山真寻のMC服务器,"+hg+",喵~------帮助页(1/3)------");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l如果发现服务器BUG请联系mahironeko97@sina.cn");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l/fastlogin  生成快速登录链接");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l/reg [邮箱] [密码] 游戏内快速注册NyaID");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l/gettoken 暂时没用awa(●'◡'●)");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l/report 举报可恶的作弊者让可爱的§l緒山まひろ §b§l去惩罚他吧喵!");
            commandSender.sendMessage("§5§l緒山まひろ §b§l»§l------------------------------------------");

        }
    }
    public static class HidePlugins extends Command {
        public HidePlugins() {//老子让你妈的查看插件了吗？喵！
            super("plugins");
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage("Only players can use this command.");
                return;
            }
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage("§5§l緒山まひろ §b§l»§4你不能这样做,喵呜!!!!!");

        }
    }
    public static class HideBungee extends Command {
        public HideBungee(){super("bungee");}
        @Override
        public void execute(CommandSender sender, String[] strings) {
            if (!(sender instanceof ProxiedPlayer)) {
                sender.sendMessage("Only players can use this command.");
                return;
            }
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.sendMessage("§5§l緒山まひろ §b§l»§6MahiroCord V1.2-Laster Edit by 星野喵奈1337    BungeeCord -MD_5 ");

        }
    }
}
