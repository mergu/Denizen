package com.denizenscript.denizen.events.world;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import org.bukkit.event.Listener;

public class TimeChangeScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // time changes (in <world>)
    // time <0-23> in <world>
    //
    // @Regex ^on time [^\s]+( in [^\s]+)$
    //
    // @Triggers when the current time changes in a world (once per mine-hour).
    //
    // @Context
    // <context.time> returns the current time (the hour, as a number from 0 to 23).
    // <context.world> returns the world.
    //
    // -->

    public TimeChangeScriptEvent() {
        instance = this;
    }

    public static TimeChangeScriptEvent instance;

    public int hour;

    public WorldTag world;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (!path.eventLower.startsWith("time")) {
            return false;
        }
        String arg1 = path.eventArgLowerAt(1);
        if (!arg1.equals("changes") && !ArgumentHelper.matchesInteger(arg1)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgLowerAt(2).equals("in") && !runGenericCheck(path.eventArgLowerAt(3), world.getName())) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public String getName() {
        return "TimeChanges";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("time")) {
            return new ElementTag(hour);
        }
        else if (name.equals("world")) {
            return world;
        }
        return super.getContext(name);
    }
}
