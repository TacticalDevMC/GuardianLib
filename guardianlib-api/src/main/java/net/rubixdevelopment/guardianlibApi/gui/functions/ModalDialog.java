package net.rubixdevelopment.guardianlibApi.gui.functions;
/*
 Created and owned by TacticalDev
 Created on 01/12/2024 at time 1:16
*/

import net.kyori.adventure.text.Component;
import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.gui.GUI;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class ModalDialog extends GUI {

    private final Consumer<Boolean> callBack;

//    private String question;
//    private Map<String, BiConsumer<Player, ModalDialog>> options = new HashMap<>();
//    private Map<String, String> descriptions;
//    private int timeoutSeconds = 0;
//    private Player player;
//    private List<Inventory> pages = new ArrayList<>();
//    private int currentPage = 0;
//    private boolean showProgressBar = false;
//    private int progressBarTicks = 0;
//
//    public ModalDialog(String question, Consumer<Boolean> callBack) {
//        super(question, 9);
//        this.callBack = callBack;
//    }
//
//    public ModalDialog addOption(String label, BiConsumer<Player, ModalDialog> action) {
//        options.put(label, action);
//        return this;
//    }
//
//    public ModalDialog addDescription(String label, String description) {
//        descriptions.put(label, description);
//        return this;
//    }
//
//    public ModalDialog setTimeout(int seconds) {
//        this.timeoutSeconds = seconds;
//        return this;
//    }
//
//    public ModalDialog setProgressBar(boolean showProgressBar, int ticks) {
//        this.showProgressBar = showProgressBar;
//        this.progressBarTicks = ticks;
//        return this;
//    }
//
//    PagedGUI pagedGUI;
//
//    private void createPage() {
//        pagedGUI = new PagedGUI("Question - Pagina " + (currentPage + 1), 9);
//
//        int slot = 0;
//        for (String label : options.keySet()) {
//            ItemBuilder optionButtonBuilder = new ItemBuilder(Material.PAPER);
//            optionButtonBuilder.setName(label);
//            if (descriptions.containsKey(label)) {
//                List<String> lore = new ArrayList<>();
//                lore.add(descriptions.get(label));
//                optionButtonBuilder.setLore(lore);
//            }
//            ItemStack optionButton = optionButtonBuilder.toItemStack();
//            pagedGUI.addButton(slot, new Button(optionButton, player -> {
//            }));
//            slot++;
//        }
//
//        if (currentPage > 0) {
//            pagedGUI.addItem(
//                    new Button(
//                            new ItemStack(Material.PAPER),
//                            player ->
//                                    player.sendMessage("You are now on page " + (currentPage + 1) + "/" + pages.size())
//                    ));
//
//            if (showProgressBar && progressBarTicks > 0) {
//                pagedGUI.addButton(4, new Button(new ItemStack(Material.GREEN_WOOL), player -> {
//                }));
//            }
//
//            pages.add(pagedGUI.getInventory());
//        }
//        pagedGUI.openFor(player);
//    }

    public ModalDialog(Plugin plugin, String question, Consumer<Boolean> callBack) {
        super(plugin, question, 1);
        this.callBack = callBack;

        addButton(3, new Button(GuardianLibProvider.get().items().builder(Material.GREEN_WOOL).name(GuardianLibProvider.get().text().color("&a&lYES")).lore(Component.text(""), GuardianLibProvider.get().text().component("&7Click to choose &a&lYES&7."), Component.text("")).build(), player -> callBack.accept(true)));
        addButton(5, new Button(GuardianLibProvider.get().items().builder(Material.RED_WOOL).name(GuardianLibProvider.get().text().color("&c&lNO")).lore(Component.text(""), GuardianLibProvider.get().text().component("&7Click to choose &c&lNO&7."), Component.text("")).build(), player -> callBack.accept(false)));
    }
}
