package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class TheNail extends BaseRelic {
    public static final String ID = MoreRelics.makeID("TheNail");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.CLINK; // TODO: Custom sound

    public TheNail() {
        super(ID, TIER, LANDING_SOUND);
    }
    
    public AbstractRelic makeCopy() {
        return new TheNail();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (card.canUpgrade()) {
            flash();
            addToBot(new UpgradeSpecificCardAction(card));
        }
    }
}
