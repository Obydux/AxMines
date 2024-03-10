package com.artillexstudios.axmines.config.impl;

import com.artillexstudios.axmines.config.AbstractConfig;
import com.artillexstudios.axmines.utils.FileUtils;

import java.util.List;

public class Messages extends AbstractConfig {

    @Key("messages.prefix")
    public String PREFIX = "<#7700FF><b>AxMines</b> <gray>»</gray> ";

    @Key("messages.reload")
    public String RELOAD = "Reloaded in <time> ms!";

    @Key("messages.reset")
    public String RESET = "Mine <mine> was automatically reset!";

    @Key("messages.selection.pos1")
    public String SELECTION_POS1 = "<white>Selected <#FF6600>POSITION #1 <gray>- <#DDDDDD><location>";

    @Key("messages.selection.pos2")
    public String SELECTION_POS2 = "<white>Selected <#FF6600>POSITION #2 <gray>- <#DDDDDD><location>";

    @Key("messages.already-exists")
    public String ALREADY_EXISTS = "<#ff0000>A mine with name <mine> already exists!";

    @Key("messages.doesnt-exist")
    public String DOESNT_EXIST = "<#ff0000>That mine doesn't exist!";

    @Key("messages.no-selection")
    public String NO_SELECTION = "<#ff0000>You haven't made a selection, or it isn't complete!";

    @Key("messages.actionbar")
    public String ACTION_BAR = "<time>    <percent>";

    @Key("messages.time.second")
    public String SECOND = "s";

    @Key("messages.time.minute")
    public String MINUTE = "m";

    @Key("messages.time.hour")
    public String HOUR = "h";

    @Key("messages.time.day")
    public String DAY = "d";

    @Key("messages.list")
    public List<String> LIST = List.of(" ", "<#7700FF><b>AxMines</b> <gray>»</gray>", " <gray>- <white>Current mines: <mines>", " ");

    @Key("messages.help")
    public List<String> HELP = List.of(" ", "<#7700FF><b>AxMines</b> <gray>»</gray>", " <gray>- <white>/axmines reload <gray>| <#7700FF>Reload the config", " <gray>- <white>/axmines wand <gray>| <#7700FF>Get a mine selection wand", " <gray>- <white>/axmines delete <gray>| <#7700FF>Delete a mine", " <gray>- <white>/axmines list <gray>| <#7700FF>List all mines", " <gray>- <white>/axmines redefine <gray>| <#7700FF>Redefine a mine", " <gray>- <white>/axmines teleport <gray>| <#7700FF>Teleport to a mine", " <gray>- <white>/axmines editor <gray>| <#7700FF>Open the mine editor", " <gray>- <white>/axmines setteleport <gray>| <#7700FF>Set the teleport location of a mine", " <gray>- <white>/axmines create <gray>| <#7700FF>Create a mine", " <gray>- <white>/axmines reset <gray>| <#7700FF>Reset a mine", "");

    @Key("messages.redefine")
    public String REDEFINE = "<green>Successfully redefined!";

    @Key("messages.teleport")
    public String TELEPORT = "<green>Teleporting!";

    @Key("messages.set-teleport")
    public String SET_TELEPORT = "<green>You've set the teleport to your location!";

    protected final String fileName;

    public Messages(String fileName) {
        this.fileName = fileName;
    }

    public void reload() {
        this.reload(FileUtils.PLUGIN_DIRECTORY.resolve(fileName), Messages.class, this, null);
    }
}