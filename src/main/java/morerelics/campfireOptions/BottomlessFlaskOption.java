package morerelics.campfireOptions;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import morerelics.MoreRelics;
import morerelics.relics.BottomlessFlask;
import morerelics.util.TextureLoader;

public class BottomlessFlaskOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(MoreRelics.makeID("BottomlessFlaskOption"));
    private static final String[] TEXT = uiStrings.TEXT;

    public BottomlessFlaskOption() {
        label = TEXT[0];
        description = TEXT[1] + BottomlessFlask.NUM_TO_BREW + TEXT[2];
        img = TextureLoader.getTexture(MoreRelics.imagePath("ui/brew.png"));
    }

    public void useOption() {
        AbstractDungeon.effectList.add(new CampfireBottomlessFlaskEffect());
    }
}
