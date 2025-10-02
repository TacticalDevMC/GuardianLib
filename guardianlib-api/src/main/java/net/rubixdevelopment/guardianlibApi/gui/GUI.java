package net.rubixdevelopment.guardianlibApi.gui;

import net.rubixdevelopment.guardianlibApi.GuardianLibProvider;
import net.rubixdevelopment.guardianlibApi.gui.button.Button;
import net.rubixdevelopment.guardianlibApi.gui.button.ConditionButton;
import net.rubixdevelopment.guardianlibApi.gui.button.functions.ClickContext;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GUI implements Listener {

    private String title;
    private int rows;
    private Inventory inventory;
    private final Map<Integer, Button> buttons = new HashMap<>();
    private Consumer<Player> onOpen;
    private Plugin plugin;

    public GUI(Plugin plugin, String title, int rows) {
        this.plugin = plugin;
        this.title = title;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows * 9, GuardianLibProvider.get().text().color(title));
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(this, plugin);
    }

    public GUI() {

    }

    public void addButton(int slot, Button button) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getIcon());
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
        inventory.setItem(slot, null);
    }

    public void clearButtons() {
        buttons.clear();
        inventory.clear();
    }

    public void clearSlot(int slot) {
        buttons.remove(slot);
        inventory.setItem(slot, null);
    }

    public void setOnOpen(Consumer<Player> onOpen) {
        this.onOpen = onOpen;
    }

    public void openFor(Player player, boolean fillWithGlass) {
        if (onOpen != null) {
            onOpen.accept(player);
        }

        getInventory().clear();

        buttons.forEach((slot, button) -> {
            if (button instanceof ConditionButton conditionButton) {
                if (conditionButton.isVisible(player)) {
                    getInventory().setItem(slot, conditionButton.getIcon());
                }
            } else {
                getInventory().setItem(slot, button.getIcon());
            }
        });
        if (fillWithGlass) {
            fillEmptySlotsWithGlass();
        }
        player.openInventory(getInventory());
    }

    public void openFor(Player player) {
        if (onOpen != null) {
            onOpen.accept(player);
        }

        getInventory().clear();

        buttons.forEach((slot, button) -> {
            if (button instanceof ConditionButton conditionButton) {
                if (conditionButton.isVisible(player)) {
                    getInventory().setItem(slot, conditionButton.getIcon());
                }
            } else {
                getInventory().setItem(slot, button.getIcon());
            }
        });
        player.openInventory(getInventory());
    }

    public void fillEmptySlotsWithGlass() {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!buttons.containsKey(i)
                    && (inventory.getItem(i) == null
                    || Objects.requireNonNull(inventory.getItem(i)).getType() == Material.AIR)) {
                inventory.setItem(i, GuardianLibProvider.get().items().builder(Material.GRAY_STAINED_GLASS_PANE).transformMeta((itemMeta -> itemMeta.setHideTooltip(true))).build());
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<Integer, Button> getButtons() {
        return buttons;
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().equals(inventory)) {
            event.setCancelled(true);

            Button button = buttons.get(event.getSlot());
            if (button != null) {

                Player player = (Player) event.getWhoClicked();
                ClickType clickType = event.getClick();
                ClickContext context = new ClickContext(player, clickType);

                if (button.getOnClick() != null) {
                    button.onClick(player);
                    return;
                }

                if (button.getClickHandlers() != null) {
                    button.onClickHandler(context);
                }
            }
        }
    }

//    public void test(Player p) {
//
//        // Maak een standaard gui
//        GUI gui = new GUI("Test GUI", 3);
//        gui.addButton(0, new Button(new ItemStack(Material.DIAMOND), player -> player.sendMessage("Dit is test 1")));
//        gui.addButton(1, new Button(new ItemStack(Material.GOLD_INGOT), player -> player.sendMessage("Dit is test 2")));
//        gui.openFor(p);
//
//        /*
//        Leg gebruikersinteracties vast
//         */
//        PlayerStatsTracker tracker = new PlayerStatsTracker();
//        GUI guiPlayerTracker = new GUI("Statistieken Menu", 3);
//        guiPlayerTracker.addButton(0, new Button(new ItemStack(Material.STONE_BUTTON), player -> {
//            tracker.trackClick(player);
//            player.sendMessage("Clicks: " + tracker.getClicks(player));
//        }));
//        gui.setOnOpen(player -> tracker.trackMenuVisit(player, gui.getTitle()));
//        gui.openFor(p);
//
//        /*
//        Navigeer eenvoudig tussen menus
//         */
//        NestedGUIManager nestedGUIManager = new NestedGUIManager();
//        GUI mainMenu = new GUI("Main Menu", 3);
//        GUI subMenu = new GUI("Sub Menu", 3);
//        mainMenu.addButton(0, new Button(new ItemStack(Material.ARROW), player -> {
//            nestedGUIManager.navigateTo(player, "subMenu");
//        }));
//        subMenu.addButton(8, new Button(new ItemStack(Material.BARRIER), nestedGUIManager::navigateBack));
//        nestedGUIManager.registerGUI("mainMenu", mainMenu);
//        nestedGUIManager.registerGUI("subMenu", subMenu);
//        nestedGUIManager.navigateTo(p, "mainMenu");
//
//        /*
//        Voeg animaties toe aan je GUI, door visuele frames te maken die na elkaar worden weergegeven.
//         */
//        AnimatedGUI animatedGUI = new AnimatedGUI("Animatie Menu", 3, 10);
//
//        // Frames toevoegen
//        ItemStack[] frame1 = {new ItemStack(Material.RED_WOOL), null, null, null, null, null, null, null, null};
//        ItemStack[] frame2 = {null, new ItemStack(Material.YELLOW_WOOL), null, null, null, null, null, null, null};
//        ItemStack[] frame3 = {null, null, new ItemStack(Material.GREEN_WOOL), null, null, null, null, null, null};
//
//        animatedGUI.addFrame(frame1);
//        animatedGUI.addFrame(frame2);
//        animatedGUI.addFrame(frame3);
//        animatedGUI.playAnimation(p);
//
//        /*
//        Add een economy fetcher met jou economy balance, hiermee kun je alles varieren, zoals kills, punten etc etc..
//         */
//        DataFetcher economyFetcher = new EconomyDataFetcher();
//        economyFetcher.fetch(p, data -> {
//            GUI economyGUI = new GUI("Economy Menu", 3);
//            economyGUI.addButton(0, new Button(new ItemStack(Material.GOLD_INGOT), player -> player.sendMessage(data)));
//            economyGUI.openFor(p);
//        });
//
//        /*
//        Pas de menu aan hoe het weer in de wereld is, zoals tijd, of locatie in de wereld.
//         */
//        GUI worldGUI = new GUI("World Menu", 3);
//        worldGUI.addButton(0, new Button(new ItemStack(Material.SUNFLOWER), player -> {
//            if (WorldConditionHandler.isDay(player.getWorld())) {
//                player.sendMessage("Its day!");
//            } else {
//                player.sendMessage("Its night!");
//            }
//        }));
//        worldGUI.addButton(1, new Button(new ItemStack(Material.WATER_BUCKET), player -> {
//            if (WorldConditionHandler.isRaining(player.getWorld())) {
//                player.sendMessage("Its raining!");
//            } else {
//                player.sendMessage("Its not raining!");
//            }
//        }));
//        worldGUI.openFor(p);
//
//        /*
//        Zet makkelijk een item in jouw hotbar slot
//         */
//        HotbarMenu hotbarMenu = new HotbarMenu();
//        hotbarMenu.setButton(0, true, true, new HotbarButton(new ItemStack(Material.DIAMOND_SWORD), player -> player.sendMessage("Sword selected!")));
//        hotbarMenu.setButton(1, true, true, new HotbarButton(new ItemStack(Material.SHIELD), player -> player.sendMessage("Shield selected!")));
//        hotbarMenu.apply(p);
//        /*
//        Zet dit in je onEnable om te zorgen dat het onClick van je HotbarMenu werkt
//
//        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
//        pluginManager.registerEvents(new HotbarMenu(), this);
//         */
//
//
//        /*
//        Voeg pagina's toe aan je menu's
//         */
//        PagedGUI pagedGUI = new PagedGUI("Paginated Menu", 3);
//
//        for (int i = 0; i <= 50; i++) {
//            int index = i;
//            pagedGUI.addItem(new Button(new ItemStack(Material.PAPER), player -> player.sendMessage("You clicked on the button: " + index)));
//        }
//
//        pagedGUI.openFor(p);
//    }
//
//    public void test2(Player p) {
//        /*
//        Voeg geluiden of particles toe wanneer je op een button klikt.
//         */
//        GUI soundParticleGUI = new GUI("Sound Particle Menu", 3);
//        soundParticleGUI.addButton(0, new Button(new ItemStack(Material.EMERALD), player -> {
//            player.sendMessage("You clicked on the button with visual effects!");
//            VisualFeedbackHelper.playSoundAndClickEffect(player, Particle.HAPPY_VILLAGER, Sound.UI_BUTTON_CLICK);
//        }));
//        soundParticleGUI.openFor(p);
//
//        /*
//        Maak buttons alleen beschikbaar voor spelers met de juiste permission
//         */
//        GUI permissionsGUI = new GUI("Permissions Menu", 3);
//
//        permissionsGUI.addButton(0, new PermissionButton(new ItemStack(Material.DIAMOND), "gui.admin", player -> player.sendMessage("Welcome, admin!")));
//        permissionsGUI.openFor(p);
//
//        /*
//        Verander jouw gui op basis van jouw spelersstatus
//
//        Hier gaan we toevoegen dat de button veranderd op je healing, hier kun je uiteraard ook veel verschillende dingen mee doen,
//        zoals custom dingen.
//         */
//        GUI healthGUI = new GUI("Health Menu", 3);
//        healthGUI.addButton(0, new DynamicButton(() -> {
//            double health = p.getHealth();
//            Material icon = health > 10 ? Material.GOLDEN_APPLE : Material.ROTTEN_FLESH;
//            String displayName = health > 10 ? "&aGenoeg health!" : "&cTe weinig health!";
//
//            return new ItemBuilder(icon).setName(displayName).toItemStack();
//        }, player -> player.sendMessage("Your health is: " + p.getHealth())));
//        healthGUI.openFor(p);
//
//        /*
//        Laat menus en buttons aanpasbaar zijn in configs.
//         */
//        Config config = new Config(Skyblock.getSB(), "menus");
//        MenuLoader menuLoader = new MenuLoader(Skyblock.getSB(), config);
//
//        GUI menuLoaderGUI = menuLoader.loadMenu("main");
//        menuLoaderGUI.openFor(p);
//
//
//        /*
//        Speel geluiden af wanneer je een menu opent, of wanneer je op een button klikt.
//         */
//        SoundEnhancedGUI soundGUI = new SoundEnhancedGUI("Sound Enhanced Menu", 3);
//        soundGUI.setOpenSound(Sound.BLOCK_CHEST_OPEN);
//        soundGUI.setClickSound(Sound.UI_BUTTON_CLICK);
//
//        soundGUI.addButton(0, new Button(new ItemStack(Material.BOOK), player -> player.sendMessage("You clicked on the button with sound!")));
//        soundGUI.openFor(p);
//
//
//        /*
//        Voeg buttons toe aan je menu, die pas tevoorschijn kunnen komen, of er pas op kunt klikken onder bepaalde voorwaarden.
//         */
//        GUI conditionalGUI = new GUI("Conditional Menu", 3);
//
//        conditionalGUI.addButton(1, new Button(new ItemStack(Material.IRON_INGOT), player -> player.sendMessage("Als je een diamond wilt zien in deze inventory, dan moet je een diamond in je hand vast hebben! Je ziet daarna een diamond in deze inventory!")));
//        conditionalGUI.addButton(2, new ConditionButton(new ItemStack(Material.DIAMOND), player -> player.getInventory().getItemInMainHand().getType() == Material.DIAMOND, player -> player.sendMessage("Je voldoet aan de voorwaarden, je hebt een diamond in je hand, dus je ziet een diamond in dees menu!")));
//
//        conditionalGUI.openFor(p);
//
//        /*
//        Laat de inhoud van de GUI live veranderen, bijvoorbeeld om een timer weer te geven.
//         */
//        LiveGUI timerGUI = new LiveGUI("Timer Menu", 3) {
//            private int seconds = 0;
//
//            @Override
//            protected void updateContents(Player player) {
//                ItemStack timerItem = new ItemBuilder(Material.CLOCK).setName("&aTimer: &7" + seconds).toItemStack();
//
//                getInventory().setItem(0, timerItem);
//                seconds++;
//            }
//        };
//        timerGUI.startUpdating(p, 20, 0);
//        timerGUI.openFor(p);
//
//        /*
//        Laat spelers navigeren tussen meerdere GUI's
//         */
//        GUI mainMenu = new GUI("Hoofd Menu", 3);
//        GUI subMenu = new GUI("Sub menu", 3);
//        subMenu.addButton(0, new Button(new ItemStack(Material.REDSTONE), player -> {
//            player.sendMessage("You are sitting in the main menu!");
//        }));
//        mainMenu.addButton(0, new NavigationButton(new ItemStack(Material.ARROW), subMenu));
//        mainMenu.openFor(p);
//
//        /*
//        Gebruik glowing items in je menu
//         */
//        ItemStack glowingItem = GlowEffectHelper.addGlow(new ItemStack(Material.DIAMOND));
//        GUI glowingGUI = new GUI("Glowing Menu", 3);
//        glowingGUI.addButton(0, new Button(glowingItem, player -> player.sendMessage("You clicked on the glowing item!")));
//        glowingGUI.openFor(p);
//
//        /*
//        Maak overgangen tussen GUI's visueel aantrekkelijk.
//         */
//        FadeEffect.fadeToGUI(p, glowingGUI, 20); // 20 ticks = 1 second
//
//        /*
//        Maak een progressbar in je GUI
//         */
//        GUI progressBarGUI = new GUI("Progress Bar", 3);
//        ProgressBar.setProgressBar(progressBarGUI, 2, 0.6, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE);
//        progressBarGUI.openFor(p);
//
//
//        /*
//        Maak een confirmation menu
//         */
//        ModalDialog questionGUI = new ModalDialog("Wil je doorgaan?", question -> {
//            if (question) {
//                p.sendMessage("Je hebt ja gekozen!");
//            } else {
//                p.sendMessage("Je hebt nee gekozen!");
//            }
//        });
//        questionGUI.openFor(p);
//
//        /*
//        AdvancedModalDialog, stel complexe vragen met meerdere opties en een callback voor de keuze.
//         */
//        Map<String, Consumer<Player>> choices = Map.of("Optie 1", player -> player.sendMessage("Je koos voor optie 1"), "Optie 2", player -> player.sendMessage("Je koos voor optie 2"), "Optie 3", player -> player.sendMessage("Je koos voor optie 3"));
//        AdvancedModalDialog dialog = new AdvancedModalDialog("Welke optie wil je kiezen?", choices);
//        dialog.openFor(p);
//
//
//        /*
//        Buttons die veranderen afhankelijke van een situatie.
//         */
//        ContextualButton healthButton = new ContextualButton(() -> new ItemStack(Material.APPLE, (int) p.getHealth()), player -> player.sendMessage("Je gezondheid is " + player.getHealth()));
//
//        ContextualButton locationButton = new ContextualButton(() -> {
//            Location loc = p.getLocation();
//            return new ItemStack(Material.COMPASS, (int) (loc.getY()));
//        }, player -> player.sendMessage("Je locatie is: " + player.getLocation()));
//
//        GUI contextGUI = new GUI("Contextual Menu", 3);
//        contextGUI.addButton(0, healthButton);
//        contextGUI.addButton(1, locationButton);
//        contextGUI.openFor(p);
//
//        /*
//        Laat meldingen zien aan spelers in een GUI, zoals waarschuwingen of informatie-updates
//         */
//        NotificationGUI notificationGUI = new NotificationGUI("Meldingen", 3, 13);
//
//        notificationGUI.addNotification("Welkom op de server!");
//        notificationGUI.addNotification("je hebt een beloning ontvangen!");
//        notificationGUI.addNotification("Let op: onderhoud om 18:00 uur.");
//        notificationGUI.openFor(p);
//
//
//        /*
//        Sla gegevens op die aan een speler zijn gekoppeld, zoals voorkeuren of instellingen
//        Dit kun je natuurlijk allemaal zelf kiezen
//         */
//        PersistentDataGUI dataMenu = new PersistentDataGUI("Data menu", 3);
//        dataMenu.addButton(10, new Button(new ItemStack(Material.BOOK), player -> {
//            dataMenu.setPlayerData(player, "voorkeur", "Donkere Thema");
//            player.sendMessage("Voorkeur opgeslagen!");
//        }));
//        dataMenu.openFor(p);
//        // Later te kunnen ophalen om de gekozen voorkeuren of instellingen of wat dan ook te kunnen veranderen op de speler's
//        // opgeslagen data
//        Object voorkeur = dataMenu.getPlayerData(p, "voorkeur");
//        p.sendMessage("Opgeslagen voorkeur: " + voorkeur);
//    }
//
//    public void test3(Player p) {
//        /*
//        Laat knoppen dynamisch gegevens weergeven die regelmatig worden bijgewerkt,
//        zoals een scoreboard of een real-time serverstatistieken.
//         */
//        LiveDataButton playerCountButton = new LiveDataButton(
//                () -> {
//                    int playerCount = Bukkit.getOnlinePlayers().size();
//                    return new ItemBuilder(Material.PLAYER_HEAD, playerCount).setName("&aSpelers online: &7" + playerCount).toItemStack();
//                },
//                player -> player.sendMessage("Bekijk hoeveel spelers er online zijn!")
//        );
//        GUI liveDataGUI = new GUI("Live data Menu", 3);
//        liveDataGUI.addButton(13, playerCountButton);
//        liveDataGUI.openFor(p);
//
//        /*
//        Laat spelers navigeren tussen verschillende menus vanuit de hoofdmenu
//         */
//        GUI mainMenu = new GUI("Hoofdmenu", 3);
//        GUI instellingenMenu = new GUI("Instellingen Menu", 3);
//
//        instellingenMenu.addButton(10, new Button(new ItemStack(Material.REDSTONE), player -> {
//            player.sendMessage("Instellingen aangepast");
//        }));
//
//        mainMenu.addButton(15, new SubMenuButton(
//                new ItemBuilder(Material.COMPASS).setName("&aOpen instellingen").toItemStack(),
//                instellingenMenu
//        ));
//        mainMenu.openFor(p);
//
//        /*
//        Voeg ondersteuning toe om met meerdere servers te communiceren via een GUI, bijvoorbeeld voor teleportaties.
//        Hier heb je een bungeecord server voor nodig!
//
//        Werkt momenteel niet.
//         */
//        CrossServerButton serverButton = new CrossServerButton(
//                new ItemStack(Material.ENDER_PEARL),
//                "Survival",
//                player -> player.sendMessage("Je wordt naar de Survival server gestuurd!")
//        );
//
//        GUI serverMenu = new GUI("Server Menu", 3);
//        serverMenu.addButton(13, serverButton);
//        serverMenu.openFor(p);
//
//
//        /*
//        Maak buttons die continu van uiterlijk veranderen, bijvoorbeeld om aandacht te trekken.
//         */
//        AnimatedButton animatedButton = new AnimatedButton(
//                Arrays.asList(
//                        new ItemBuilder(Material.RED_WOOL).setName("&cFrame 1").toItemStack(),
//                        new ItemBuilder(Material.GREEN_WOOL).setName("&aFrame 2").toItemStack(),
//                        new ItemBuilder(Material.BLUE_WOOL).setName("&1Frame 3").toItemStack()
//                ),
//                player -> player.sendMessage("je hebt op de geanimeerde knop gedrukt!")
//        );
//        GUI animatedGUI = new GUI("Animated Menu", 3);
//        animatedGUI.addButton(13, animatedButton);
//        animatedGUI.openFor(p);
//
//
//        /*
//        Pas automatisch de grote aan van de GUI op basis van het aantal buttons of items.
//         */
//        DynamicSizeGUI dynamicSizeGUI = new DynamicSizeGUI("Auto size Menu");
//
//        for (int i = 0; i < 25; i++) {
//            int index = i;
//            dynamicSizeGUI.addButton(i, new Button(
//                    new ItemBuilder(Material.PAPER).setName("&aItem #" + (i + 1)).toItemStack(),
//                    player -> player.sendMessage("Je hebt item #" + (index + 1) + " geselecteerd!")
//            ));
//        }
//        dynamicSizeGUI.openFor(p);
//
//        /*
//        Voeg buttons toe die spelers pas na een bepaalde tijd kunnen gebruiken.
//         */
//        TimedRewardButton rewardButton = new TimedRewardButton(
//                new ItemBuilder(Material.GOLDEN_APPLE).toItemStack(),
//                60,
//                player -> player.sendMessage("Je hebt een belonign ontvangen.")
//        );
//        GUI rewardGUI = new GUI("Timed Reward Menu", 3);
//        rewardGUI.addButton(13, rewardButton);
//        rewardGUI.openFor(p);
//
//        /*
//        Functie om te kijken hoeveel keer een button in de GUI wordt aangeklikt, dit kan handig zijn
//        voor als je een server maakt, en je wilt weten hoeveel mensen naar welke gamemode gaat.
//         */
//        AnalyticsButton analyticsButton = new AnalyticsButton(
//                "button1",
//                new ItemBuilder(Material.BOOK).setName("Klik hier").toItemStack(),
//                player -> player.sendMessage("Je hebt op de button geklikt!")
//        );
//        GUI analyticsGUI = new GUI("Analytics Menu", 3);
//        analyticsGUI.addButton(13, analyticsButton);
//
//        p.sendMessage("Deze button is " + AnalyticsButton.getClickCounts("button1") + " keer geklikt!");
//        analyticsGUI.openFor(p);
//    }
}