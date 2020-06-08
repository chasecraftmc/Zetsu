package me.blazingtide.zetsu;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final Zetsu zetsu = new Zetsu(this);

        zetsu.registerCommands();
    }
}
