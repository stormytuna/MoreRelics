package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;
import morerelics.powers.MoldedSandPower;

public class MoldedSand extends BaseRelic {
    public static final String ID = MoreRelics.makeID("MoldedSand");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;

    public MoldedSand() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new MoldedSand();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MoldedSandPower(AbstractDungeon.player)));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
