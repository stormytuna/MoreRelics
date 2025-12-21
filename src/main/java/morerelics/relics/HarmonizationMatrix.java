package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class HarmonizationMatrix extends BaseRelic {
    public static final String ID = MoreRelics.makeID("HarmonizationMatrix");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    public static final int TURNS_TO_APPLY_FOCUS = 3;

    public HarmonizationMatrix() {
        super(ID, TIER, LANDING_SOUND);
        counter = 3;
    }

    public AbstractRelic makeCopy() {
        return new HarmonizationMatrix();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TURNS_TO_APPLY_FOCUS + DESCRIPTIONS[1];
    }

    public void atTurnStart() {
        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                grayscale = true;
            }

            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1)));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public void onVictory() {
        atBattleStart();
    }

    public void atBattleStart() {
        counter = TURNS_TO_APPLY_FOCUS;
        grayscale = false;
    }
}
