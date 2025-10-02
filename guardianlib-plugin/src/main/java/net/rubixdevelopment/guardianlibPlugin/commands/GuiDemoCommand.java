package net.rubixdevelopment.guardianlibPlugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import net.rubixdevelopment.guardianlibApi.GuardianLib;
import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.gui.GUI;
import net.rubixdevelopment.guardianlibApi.gui.GuardianGui;
import net.rubixdevelopment.guardianlibApi.gui.PagedGUI;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import net.rubixdevelopment.guardianlibApi.gui.functions.ModalDialog;
import net.rubixdevelopment.guardianlibPlugin.impl.builder.ProvidedItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@CommandAlias("gui")
@Description("Demo- en utility-commando's voor Guardian GUI's")
public class GuiDemoCommand extends BaseCommand {

    private final Plugin plugin;

    public GuiDemoCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Default
    @Subcommand("demo")
    @CommandPermission("guardian.gui.demo")
    @Description("Open een basisdemo met knoppen")
    public void onDemo(Player player) {
        GUI gui = GuardianGui.create(plugin, "&aGUI Demo", 3);

        // Basic onClick
        GuardianGui.addButton(gui, 10,
                GuardianLibProvider.get().items().builder(Material.EMERALD).name("&aKlik!").build(),
                p -> p.sendMessage(GuardianLib.get().text().component("&7Je klikte op de &aEMERALD&7!"))
        );

        // ClickType handler
        GuardianGui.addButtonWithClickType(gui, 12,
                GuardianLibProvider.get().items().builder(Material.PAPER).name("&eClickType test").build(),
                ctx -> ctx.getPlayer().sendMessage(GuardianLib.get().text().component("ClickType: " + ctx.getClick()))
        );

        // Sluitmenu knop
        GuardianGui.addButtonClose(gui, 16,
                GuardianLibProvider.get().items().builder(Material.BARRIER).name("&cSluiten").build(),
                p -> p.sendMessage(Component.text("Menu gesloten."))
        );

        GuardianGui.open(gui, player, true);
    }

    @Subcommand("paged")
    @CommandPermission("guardian.gui.paged")
    @Description("Open een voorbeeld met PagedGUI")
    public void onPaged(Player player) {
        PagedGUI paged = new PagedGUI(plugin, "&ePaginated Voorbeeld", 3);

        for (int i = 0; i < 50; i++) {
            int idx = i;
            paged.addItem(new Button(
                    GuardianLibProvider.get().items().builder(Material.PAPER).name("&7Item #" + (idx + 1)).build(),
                    p -> p.sendMessage(Component.text("Je klikte item #" + (idx + 1)))
            ));
        }

        // Pagina 0 tonen + openen
        paged.showPage(0);
        paged.openFor(player);
    }

    @Subcommand("modal")
    @CommandPermission("guardian.gui.modal")
    @Description("Toont een ModalDialog met YES/NO callback")
    public void onModal(Player player) {
        ModalDialog dialog = new ModalDialog(plugin, "&bWil je doorgaan?",
                choice -> {
                    if (choice) player.sendMessage(Component.text("Je koos: JA"));
                    else player.sendMessage(Component.text("Je koos: NEE"));
                }
        );
        dialog.openFor(player, true);
    }

    @Subcommand("condition")
    @CommandPermission("guardian.gui.condition")
    @Description("Toont een ConditionButton die alleen zichtbaar is met diamond in main hand")
    public void onCondition(Player player) {
        GUI gui = GuardianGui.create(plugin, "&bVoorwaarden Menu", 3);

        GuardianGui.addConditionButton(
                gui,
                13,
                GuardianLibProvider.get().items().builder(Material.DIAMOND)
                        .name("&bAlleen zichtbaar met diamond in je hand")
                        .build(),
                p -> p.getInventory().getItemInMainHand().getType() == Material.DIAMOND,
                p -> p.sendMessage(Component.text("Voorwaarde behaald!"))
        );

        GuardianGui.open(gui, player, true);
    }
}