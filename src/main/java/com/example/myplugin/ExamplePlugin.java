package com.example.myplugin;

import com.example.myplugin.commands.ExampleCommand;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class ExamplePlugin extends JavaPlugin {
    public ExamplePlugin(JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand());
    }
}
