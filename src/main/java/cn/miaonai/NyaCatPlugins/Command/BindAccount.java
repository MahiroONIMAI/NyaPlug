package cn.miaonai.NyaCatPlugins.Command;

import cn.miaonai.NyaCatPlugins.NyaCatPlugins;
import cn.miaonai.NyaCatPlugins.Packet.C31;
import cn.miaonai.NyaCatPlugins.Util.RandomEv;
import com.alibaba.fastjson2.JSONObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class BindAccount extends Command implements Listener {
    public BindAccount(NyaCatPlugins nyaCatPlugins) {
        super("bindweb");
        this.nyaCatPlugins = nyaCatPlugins;
    }

    private final NyaCatPlugins nyaCatPlugins;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        String Code = RandomEv.RandomBindID();
        C31 c31 = new C31();
        c31.setCode(Code);
        c31.setUuid(player.getUniqueId().toString());
        nyaCatPlugins.sendWebSocketMessage(JSONObject.toJSONString(c31));
        player.sendMessage("§5§l小鳥遊ホシノ §b§l»§l§6The binding code has been obtained, and your binding code is as follows:§c§l»"+Code+"§l§6This binding code is valid for 180 seconds!!");
        }
}

