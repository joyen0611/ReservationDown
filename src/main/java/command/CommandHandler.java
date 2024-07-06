package command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.example.pluhin.reservationdown.ReservationDown;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandExecutor {
    private final ReservationDown plugin;
    private BukkitTask countdownTask;
    Count second;

    public CommandHandler(ReservationDown plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;
        if (args[0].equals("check")) return check(sender);
        if (!sender.isOp()) {
            sender.sendMessage("This command can only be used by operators.");
            return true;
        }

        if (args[0].equals("remove")) return remove(sender);
        if (args[0].equals("set") && args.length > 1) return set(sender, args[1], Integer.valueOf(args[2]));
        return false;
    }
    public boolean set(CommandSender sender, String unit, Integer time) {
        int times;
        switch (unit) {
            case "hour" -> times = time * 3600;
            case "minute" -> times = time * 60;
            case "second" -> times = time;
            default -> {
                return false;
            }
        }
        second = new Count(times);
        long ticks = times * 20L;

        countdownTask = new BukkitRunnable() {
            int value = times;
            int count = 3;

            @Override
            public void run() {
                if (value == 0) {
                    this.cancel(); // 카운트다운 완료 후 작업 취소
                    Bukkit.shutdown();
                } else if (value == 2) {
                    sendTitleToAllPlayers("이용자 여러분 수고 하셨습니다.", "", 10, 40, 10);
                } else if (2 < value && value < 6) {
                    sendTitleToAllPlayers("서버 재시작까지", String.valueOf(count), 0, 21, 0);
                    count--;
                } else if (value == 600) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "[서버 공지] " + ChatColor.WHITE + "서버 종료 까지 약 10분 남았습니다.");
                }
                value--;
                second.decrease();
            }
        }.runTaskTimer(plugin, 0, 20); // 20틱 간격으로 반복

        sender.sendMessage("성공적으로 종료작업이 예약 되었습니다.");
        return true;
    }
    public boolean remove(CommandSender sender) {
        if (countdownTask == null) {
            sender.sendMessage("예약된 종료작업이 없습니다!");
            return true;
        }
        countdownTask.cancel();
        second = null;
        countdownTask = null;
        sender.sendMessage("예약된 종료작업이 취소되었습니다!");
        return true;
    }
    public boolean check(CommandSender sender) {
        if (second == null) {
            sender.sendMessage("예약된 종료작업이 없습니다!");
            return true;
        }
        sender.sendMessage(SecondsToTime(second.get()));
        return true;
    }
    public static String SecondsToTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("서버 종료까지 %d시간 %d분 %d초 남았습니다.", hours, minutes, seconds);
    }
    public void sendTitleToAllPlayers(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}
