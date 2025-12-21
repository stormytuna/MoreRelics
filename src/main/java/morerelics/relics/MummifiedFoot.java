package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import morerelics.MoreRelics;

public class MummifiedFoot extends BaseRelic {
    public static final String ID = MoreRelics.makeID("MummifiedFoot");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT; // TODO: Custom sound
    private static boolean madeCardFreeThisCombat = true;

    public MummifiedFoot() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new MummifiedFoot();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (madeCardFreeThisCombat) {
            return;
        }

        madeCardFreeThisCombat = true;

        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        pulse = false;
        grayscale = true;
    }

    public void onEnterRoom(AbstractRoom room) {
        madeCardFreeThisCombat = false;

        flash();
        beginLongPulse();
        grayscale = false;
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
