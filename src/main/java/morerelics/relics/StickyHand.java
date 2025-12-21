package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class StickyHand extends BaseRelic {
    public static final String ID = MoreRelics.makeID("StickyHand");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT; // TODO: Custom sound
    public static final int DRAW_AMOUNT = 1;

    public StickyHand() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new StickyHand();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW_AMOUNT + DESCRIPTIONS[1];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (!card.purgeOnUse && card.isInnate) {
            this.flash();
            addToBot(new DrawCardAction(DRAW_AMOUNT));
        }
    }
}
