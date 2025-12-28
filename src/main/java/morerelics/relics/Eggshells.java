package morerelics.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class Eggshells extends BaseRelic {
    public static final String ID = MoreRelics.makeID("Eggshells");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public Eggshells() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new Eggshells();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (GameActionManager.turn == 1) {
            flash();
            if (drawnCard.canUpgrade()) {
                addToTop(new UpgradeSpecificCardAction(drawnCard));
            }
        }
    }
}
