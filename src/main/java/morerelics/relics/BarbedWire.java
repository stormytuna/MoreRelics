package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class BarbedWire extends BaseRelic {
    public static final String ID = MoreRelics.makeID("BarbedWire");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    public static final int THORNS_AMOUNT = 1;

    public BarbedWire() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new BarbedWire();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + THORNS_AMOUNT + DESCRIPTIONS[1];
    }

    public void atTurnStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, THORNS_AMOUNT)));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
