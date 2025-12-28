package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.stances.AbstractStance;
import morerelics.MoreRelics;

public class CrimsonLily extends BaseRelic {
    public static final String ID = MoreRelics.makeID("CrimsonLily");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;
    public static final int STRENGTH_GAIN = 5;

    public CrimsonLily() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new CrimsonLily();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH_GAIN + DESCRIPTIONS[1];
    }

    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
        if (!prevStance.ID.equals(newStance.ID) && newStance.ID.equals("Wrath")) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH_GAIN), STRENGTH_GAIN));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, STRENGTH_GAIN), STRENGTH_GAIN));
        }
    }
}
