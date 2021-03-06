package robmart.mods.targetingapi.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.Faction;
import robmart.mods.targetingapi.api.faction.IFaction;

public class CommandFactions {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("factions")
                        .requires(s -> s.hasPermissionLevel(3))
                        .then(Commands.literal("add")
                                .then(Commands.argument("faction", StringArgumentType.string())
                                        .suggests((ctx, builder) -> ISuggestionProvider.suggest(Targeting.getFactionMap().keySet(), builder))
                                        .then(Commands.argument("player", EntityArgument.players())
                                                .executes((ctx) -> addToFaction(StringArgumentType.getString(ctx, "faction"),
                                                        (PlayerEntity) EntityArgument.getEntity(ctx, "player")))))
                        ).then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.players())
                                .executes((context -> listFactions(context.getSource(), (PlayerEntity) EntityArgument.getEntity(context, "player")))))
                )

        );
    }

    private static int listFactions(CommandSource source, PlayerEntity entity) {
        source.sendFeedback(new StringTextComponent(Targeting.getFactionsFromEntity(entity).toString()), true);
        return 1;
    }

    private static int addToFaction(String faction, PlayerEntity entity) throws CommandSyntaxException {
        Faction iFaction = (Faction) Targeting.getFaction(faction);
        iFaction.addMemberEntity(entity);
        return 1;
    }
}
