package morerelics.relics;

import java.util.ArrayList;
import java.util.Random;

import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import basemod.BaseMod;
import morerelics.MoreRelics;

public class Memento extends BaseRelic {
    public static final String ID = MoreRelics.makeID("Memento");
    public static final RelicTier TIER = RelicTier.SHOP;
    public static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public Memento() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new Memento();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        ArrayList<AbstractCard> cardPool = new ArrayList<>();
        AbstractDungeon.player.getCardPool(cardPool);
        cardPool.sort((a, b) -> { 
            if (a.rarity == b.rarity) {
                return a.name.compareTo(b.name);
            }

            if (a.rarity == CardRarity.RARE || a.rarity == CardRarity.UNCOMMON && b.rarity == CardRarity.COMMON) {
                return -1;
            }

            return 1;
        });

        CardGroup cards = new CardGroup(CardGroupType.UNSPECIFIED);
        for (AbstractCard c : cardPool) {
            cards.addToTop(c);
        }

        UIStrings tooltips = CardCrawlGame.languagePack.getUIString(MoreRelics.makeID("Memento"));
        int chosenTooltipIndex = new Random().nextInt(tooltips.TEXT.length);
        BaseMod.openCustomGridScreen(cards, 1, tooltips.TEXT[chosenTooltipIndex], c -> {
            AbstractCard chosenCard = c.get(0);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(chosenCard, Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        });
        AbstractDungeon.overlayMenu.cancelButton.hide();
    }
}
