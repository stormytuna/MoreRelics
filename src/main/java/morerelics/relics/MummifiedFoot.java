package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class MummifiedFoot extends BaseRelic {
    public static final String ID = MoreRelics.makeID("MummifiedFoot");
    public static final RelicTier TIER = RelicTier.COMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static boolean madeCardFreeThisCombat = true;

    public MummifiedFoot() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new MummifiedFoot();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (madeCardFreeThisCombat) {
            return;
        }

        madeCardFreeThisCombat = true;

        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        pulse = false;
    }

    public void onVictory() {
        madeCardFreeThisCombat = true;
        pulse = false;
    }

    public void atBattleStartPreDraw() {
        madeCardFreeThisCombat = false;

        flash();
        beginLongPulse();
    }

    @SpirePatch(clz = AbstractCard.class, method = "freeToPlay")
    public static class MakeFirstCardFreePatch {
        @SpirePostfixPatch
        public static boolean patch(boolean __result, AbstractCard __instance) {
            if (!madeCardFreeThisCombat) {
                return true;
            } 

            return __result;
        }
    }

    
}
