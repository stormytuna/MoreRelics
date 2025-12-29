package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class CorkStopper extends BaseRelic {
    public static final String ID = MoreRelics.makeID("CorkStopper");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.SOLID;
    public static final int DRAW_AMOUNT = 1;

    public CorkStopper() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new CorkStopper();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW_AMOUNT + DESCRIPTIONS[1];
    }

    public void onUsePotion() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new DrawCardAction(DRAW_AMOUNT));
    }
}
