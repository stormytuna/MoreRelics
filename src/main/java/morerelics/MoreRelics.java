package morerelics;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import morerelics.relics.AdamantineHammer;
import morerelics.relics.BarbedWire;
import morerelics.relics.BeckoningSnecko;
import morerelics.relics.BottomlessFlask;
import morerelics.relics.CloakOfDisplacement;
import morerelics.relics.CorkStopper;
import morerelics.relics.CrimsonLily;
import morerelics.relics.DullShard;
import morerelics.relics.Eggshells;
import morerelics.relics.HarmonizationMatrix;
import morerelics.relics.LetterpressStamp;
import morerelics.relics.LineGraph;
import morerelics.relics.Lubricant;
import morerelics.relics.Memento;
import morerelics.relics.MoldedSand;
import morerelics.relics.MummifiedFoot;
import morerelics.relics.ProstheticLimb;
import morerelics.relics.PuritySeal;
import morerelics.relics.StickyHand;
import morerelics.relics.StuffedBird;
import morerelics.relics.TheNail;
import morerelics.relics.TheNeedle;
import morerelics.relics.TinBracelet;
import morerelics.relics.TrainingWheels;
import morerelics.util.GeneralUtils;
import morerelics.util.KeywordInfo;
import morerelics.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class MoreRelics implements
        EditStringsSubscriber,
        EditRelicsSubscriber,
        PostInitializeSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    private static final String CONFIG_PATH = "preferences/morerelics.cfg";
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    private static TreeMap<String, HashSet<String>> config = new TreeMap<>();
    private static HashSet<String> disabled = new HashSet<>();

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new MoreRelics();
    }

    public MoreRelics() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    private static void saveConfig() {
        String sConfig = new Gson().toJson(config);
        Gdx.files.local(CONFIG_PATH).writeString(sConfig, false, String.valueOf(StandardCharsets.UTF_8));
        logger.info("saved config=");
    }

    private static void loadConfig() {
        if (Gdx.files.local(CONFIG_PATH).exists()) {
            String sConfig = Gdx.files.local(CONFIG_PATH).readString(String.valueOf(StandardCharsets.UTF_8));
            logger.info("loaded config=" + sConfig);
            Type mapType = (new TypeToken<TreeMap<String, HashSet<String>>>() {
            }.getType());
            config = new Gson().fromJson(sConfig, mapType);
            disabled = config.get("disabled");
        } else {
            config.put("disabled", disabled);
        }
    }

    public static boolean isEnabled(String relicID) {
        return !disabled.contains(relicID);
    }

    private static float getXPos(int index) {
        return 410.0F + 320.0F * (index / 10);
    }

    private static float getYPos(int index) {
        return 660.0F - 50F * (index % 10);
    }

    private static UIStrings getUIStrings(String uiName) {
        return CardCrawlGame.languagePack.getUIString(makeID(uiName));
    }

    @Override
    public void receivePostInitialize() {
        loadConfig();

        ModPanel configPanel = new ModPanel();
        String labelText = getUIStrings("DisabledRelics").TEXT[0];
        ModLabel disabledLabel = new ModLabel(labelText, 400f, 730f, configPanel, label -> { });
        configPanel.addUIElement(disabledLabel);

        String[] allRelicIds = {
            AdamantineHammer.ID,
            BarbedWire.ID,
            BottomlessFlask.ID,
            CloakOfDisplacement.ID,
            CorkStopper.ID,
            DullShard.ID,
            Eggshells.ID,
            LetterpressStamp.ID,
            LineGraph.ID,
            Memento.ID,
            MoldedSand.ID,
            MummifiedFoot.ID,
            ProstheticLimb.ID,
            PuritySeal.ID,
            StickyHand.ID,
            StuffedBird.ID,
            TheNail.ID,
            TheNeedle.ID,
            TinBracelet.ID,
            TrainingWheels.ID,
            HarmonizationMatrix.ID,
            Lubricant.ID,
            BeckoningSnecko.ID,
            CrimsonLily.ID,
        };

        for (int i = 0; i < allRelicIds.length; i++) {
            String relicID = allRelicIds[i];
            ModLabeledToggleButton disableButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getRelicStrings(relicID).NAME, getXPos(i), getYPos(i), Settings.CREAM_COLOR, FontHelper.charDescFont, !isEnabled(relicID), configPanel, label -> { }, button -> {
                if (button.enabled) {
                    disabled.add(relicID);
                } else {
                    disabled.remove(relicID);
                }

                saveConfig();
            });

            configPanel.addUIElement(disableButton);
        }

        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, configPanel);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new AdamantineHammer(), RelicType.SHARED);
        BaseMod.addRelic(new BarbedWire(), RelicType.SHARED);
        BaseMod.addRelic(new BottomlessFlask(), RelicType.SHARED);
        BaseMod.addRelic(new CloakOfDisplacement(), RelicType.SHARED);
        BaseMod.addRelic(new CorkStopper(), RelicType.SHARED);
        BaseMod.addRelic(new DullShard(), RelicType.SHARED);
        BaseMod.addRelic(new Eggshells(), RelicType.SHARED);
        BaseMod.addRelic(new LetterpressStamp(), RelicType.SHARED);
        BaseMod.addRelic(new LineGraph(), RelicType.SHARED);
        BaseMod.addRelic(new Memento(), RelicType.SHARED);
        BaseMod.addRelic(new MoldedSand(), RelicType.SHARED);
        BaseMod.addRelic(new MummifiedFoot(), RelicType.SHARED);
        BaseMod.addRelic(new ProstheticLimb(), RelicType.SHARED);
        BaseMod.addRelic(new PuritySeal(), RelicType.SHARED);
        BaseMod.addRelic(new StickyHand(), RelicType.SHARED);
        BaseMod.addRelic(new StuffedBird(), RelicType.SHARED);
        BaseMod.addRelic(new TheNail(), RelicType.SHARED);
        BaseMod.addRelic(new TheNeedle(), RelicType.SHARED);
        BaseMod.addRelic(new TinBracelet(), RelicType.SHARED);
        BaseMod.addRelic(new TrainingWheels(), RelicType.SHARED);
        BaseMod.addRelic(new HarmonizationMatrix(), RelicType.BLUE);
        BaseMod.addRelic(new Lubricant(), RelicType.BLUE);
        BaseMod.addRelic(new BeckoningSnecko(), RelicType.PURPLE);
        BaseMod.addRelic(new CrimsonLily(), RelicType.PURPLE);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            }
            catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String audioPath(String file) {
        return resourcesFolder + "/audio/" + file;
    }
    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }
    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }
    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = MoreRelics.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        if (separator > 0)
            name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + MoreRelics.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(MoreRelics.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
