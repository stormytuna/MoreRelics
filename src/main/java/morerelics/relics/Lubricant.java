package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import morerelics.MoreRelics;

public class Lubricant extends BaseRelic {
    public static final String ID = MoreRelics.makeID("Lubricant");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT; // TODO: Custom sound

    private boolean drawnCardThisTurn = true;

    public Lubricant() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new Lubricant();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (!drawnCardThisTurn && c.cost == 0 && !c.purgeOnUse) {
            flash();
            pulse = false;
            drawnCardThisTurn = true;
            addToTop(new DrawCardAction(1));
        }
    }

    public void onVictory() {
        pulse = false;
    }

    public void atTurnStart() {
        drawnCardThisTurn = false;
        beginLongPulse();
    }
}
