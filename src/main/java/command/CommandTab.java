package command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTab implements TabCompleter {
    private static final List<String> FIRST_ARGS = Arrays.asList("check","remove", "set");
    private static final List<String> SET_ARGS = Arrays.asList("hour", "minute", "second");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            return Arrays.asList("check");
        }if (args.length == 1) {
            return getMatchingArgs(args[0], FIRST_ARGS);
        }else if (args.length == 2 && args[0].equals("set")) {
            return getMatchingArgs(args[1], SET_ARGS);
        }else if (args.length == 3 && args[0].equals("set")) {
            return List.of("<time>");
        }
        return new ArrayList<>();
    }
    private List<String> getMatchingArgs(String arg, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(arg.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
}
