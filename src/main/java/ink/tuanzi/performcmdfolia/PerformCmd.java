package ink.tuanzi.performcmdfolia;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.tcoded.folialib.FoliaLib;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PerformCmd extends JavaPlugin {

    private final FoliaLib foliaLib = new FoliaLib(this);

    @Override
    public void onEnable() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("perfcmd")
                .requires(sender -> sender.getSender().hasPermission("perfcmd.use"));

        root.then(commandHandler(Commands.literal("asop"), PERFORM_ROLE.OP));
        root.then(commandHandler(Commands.literal("asplayer"), PERFORM_ROLE.DEFAULT));

        LiteralCommandNode<CommandSourceStack> cmdNode = root.build();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(cmdNode);
        });
    }

    private LiteralArgumentBuilder<CommandSourceStack> commandHandler(LiteralArgumentBuilder<CommandSourceStack> builder, PERFORM_ROLE role) {
        return builder
                .requires(sender -> sender.getSender().hasPermission(role.permission))
                .then(Commands.argument("target", ArgumentTypes.player())
                        .then(Commands.argument("command", StringArgumentType.greedyString())
                                .executes((ctx) -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    final Player target = targetResolver.resolve(ctx.getSource()).get(0);

                                    final String command = ctx.getArgument("command", String.class);

                                    perfCommand(target, role, command);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                );
    }

    private void perfCommand(Player player, PERFORM_ROLE role, String command) {
        String pureCommand = command.startsWith("/") ? command.substring(1) : command;
        getLogger().info(String.format("%s[%s] perform a command as %s: /%s", player.getName(), player.getUniqueId(), role, command));

        if (PERFORM_ROLE.OP.equals(role)) {
            this.foliaLib.getScheduler().runAtEntity(player, (task) -> {
                AtomicBoolean isOp = new AtomicBoolean(player.isOp()); // before status
                try {
                    player.setOp(true);
                    player.performCommand(pureCommand);
                } finally {
                    player.setOp(isOp.get());
                }
            });
        } else {
            this.foliaLib.getScheduler().runAtEntity(player, (task) -> {
                player.performCommand(pureCommand);
            });
        }
    }

    enum PERFORM_ROLE {
        OP("perfcmd.command.asop"),
        DEFAULT("perfcmd.command.asplayer");

        final String permission;

        PERFORM_ROLE(String s) {
            this.permission = s;
        }
    }
}
