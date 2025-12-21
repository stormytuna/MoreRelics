package morerelics;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import morerelics.relics.AdamantineHammer;
import morerelics.relics.BeckoningSnecko;
import morerelics.relics.CloakOfDisplacement;
import morerelics.relics.CrimsonLily;
import morerelics.relics.Eggshells;
import morerelics.relics.HarmonizationMatrix;
import morerelics.relics.LetterpressStamp;
import morerelics.relics.LineGraph;
import morerelics.relics.Lubricant;
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
import morerelics.util.Sounds;
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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

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

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //You can find information about this on the BaseMod wiki page "Mod Config and Panel".
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new Eggshells(), RelicType.SHARED);
        BaseMod.addRelic(new TheNail(), RelicType.SHARED);
        BaseMod.addRelic(new TheNeedle(), RelicType.SHARED);
        BaseMod.addRelic(new MoldedSand(), RelicType.SHARED);
        BaseMod.addRelic(new LineGraph(), RelicType.SHARED);
        BaseMod.addRelic(new LetterpressStamp(), RelicType.SHARED);
        BaseMod.addRelic(new MummifiedFoot(), RelicType.SHARED);
        BaseMod.addRelic(new CloakOfDisplacement(), RelicType.SHARED);
        BaseMod.addRelic(new StickyHand(), RelicType.SHARED);
        BaseMod.addRelic(new AdamantineHammer(), RelicType.SHARED);
        BaseMod.addRelic(new TrainingWheels(), RelicType.SHARED);
        BaseMod.addRelic(new StuffedBird(), RelicType.SHARED);
        BaseMod.addRelic(new PuritySeal(), RelicType.SHARED);
        BaseMod.addRelic(new TinBracelet(), RelicType.SHARED);
        BaseMod.addRelic(new ProstheticLimb(), RelicType.SHARED);
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
