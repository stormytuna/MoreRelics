package morerelics.relics;

import java.util.Random;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.Parasite;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class StuffedBird extends BaseRelic {
    public static final String ID = MoreRelics.makeID("StuffedBird");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    public static final AbstractCard[] ALLOWED_CURSES = {
        new Clumsy(),
        new Decay(),
        new Doubt(),
        new Injury(),
        new Normality(),
        new Pain(),
        new Parasite(),
        new Regret(),
        new Shame(),
        new Writhe(),
    };

    public static int cursesToAdd = 0;

    public StuffedBird() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new StuffedBird();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    public void update() {
        super.update();

        if (cursesToAdd > 0) {
            flash();

            CardGroup group = new CardGroup(CardGroupType.UNSPECIFIED);

            for (int i = 0; i < cursesToAdd; i++) {
                int chosenCurseIndex = AbstractDungeon.cardRandomRng.random(ALLOWED_CURSES.length - 1);
                AbstractCard curse = ALLOWED_CURSES[chosenCurseIndex].makeCopy();
                group.addToBottom(curse);
            }

            cursesToAdd = 0;

            UIStrings tooltips = CardCrawlGame.languagePack.getUIString(MoreRelics.makeID("StuffedBird"));
            int chosenTooltipIndex = new Random().nextInt(tooltips.TEXT.length);
            AbstractDungeon.gridSelectScreen.openConfirmationGrid(group, tooltips.TEXT[chosenTooltipIndex]);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = { AbstractCard.class })
    public static class AddCursesWhenRemovingCardsPatch {
        @SpirePostfixPatch
        public static void patch(CardGroup __instance, AbstractCard card) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(StuffedBird.ID);
            if (relic != null && __instance.type == CardGroupType.MASTER_DECK) {
                // Can't add here as inserting cards causes a ConcurrentModificationException
                StuffedBird.cursesToAdd++;
            }
        }
    }
}
