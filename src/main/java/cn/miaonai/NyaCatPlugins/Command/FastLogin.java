package cn.miaonai.NyaCatPlugins.Command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class FastLogin extends Command implements Listener {
    public FastLogin() {
        super("fastlogin");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("Only players can use this command.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

    }
}