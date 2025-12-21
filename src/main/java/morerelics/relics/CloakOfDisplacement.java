package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class CloakOfDisplacement extends BaseRelic {
    public static final String ID = MoreRelics.makeID("CloakOfDisplacement");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;
    public static final int TURNS_TO_APPLY = 2;

    public CloakOfDisplacement() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new CloakOfDisplacement();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURNS_TO_APPLY +  DESCRIPTIONS[1];
    }

    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, TURNS_TO_APPLY)));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }
}
