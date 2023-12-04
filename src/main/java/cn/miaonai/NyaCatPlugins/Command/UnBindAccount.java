package cn.miaonai.NyaCatPlugins.Command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public  class UnBindAccount extends Command  implements Listener {
    public UnBindAccount() {
        super("unbindweb");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage("Only players can use this command.");
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;



    }


}