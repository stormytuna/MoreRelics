package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class BeckoningSnecko extends BaseRelic {
    public static final String ID = MoreRelics.makeID("BeckoningSnecko");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.MAGICAL;
    public static final int NEW_MANTRA_AMOUNT = 8;

    public BeckoningSnecko() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new BeckoningSnecko();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + NEW_MANTRA_AMOUNT +  DESCRIPTIONS[1];
    }

    @SpirePatch(clz = MantraPower.class, method = "stackPower")
    public static class MakeMantraStackToEight {
        @SpirePostfixPatch
        public static void patch(MantraPower __instance, int stackAmount) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(BeckoningSnecko.ID);
            if (relic == null) {
                return;
            }

            if (__instance.amount >= NEW_MANTRA_AMOUNT) {
                relic.flash();
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));

                AbstractDungeon.actionManager.addToTop(new ChangeStanceAction("Divinity"));
                __instance.amount -= NEW_MANTRA_AMOUNT;
                if (__instance.amount <= 0) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, __instance));
                }
            }
        }
    }
    
}
