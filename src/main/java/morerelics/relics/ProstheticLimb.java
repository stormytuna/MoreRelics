package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.BaseMod;
import morerelics.MoreRelics;

public class ProstheticLimb extends BaseRelic {
    public static final String ID = MoreRelics.makeID("ProstheticLimb");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.HEAVY;
    public static final int DRAW = 1;
    public static final int EXTRA_HAND_SIZE = 1;

    public ProstheticLimb() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new ProstheticLimb();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW + DESCRIPTIONS[1] + EXTRA_HAND_SIZE + DESCRIPTIONS[2];
    }

    public void atTurnStart() {
        flash();
        addToTop(new DrawCardAction(DRAW));
    }

    public void onEquip() {
        BaseMod.MAX_HAND_SIZE += EXTRA_HAND_SIZE;
    }
}
