package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import morerelics.MoreRelics;
import morerelics.powers.ZealPower;

public class PuritySeal extends BaseRelic {
    public static final String ID = MoreRelics.makeID("PuritySeal");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT; // TODO: Custom sound maybe
    public static final int ZEAL_AMOUNT = 2;

    public PuritySeal() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new PuritySeal();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ZEAL_AMOUNT + DESCRIPTIONS[1];
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    // Applying after all other battle start relics, but before turn start relics
    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class ApplyPuritySealZealPowerPatch {
        @SpireInsertPatch(rloc = 41)
        public static void patch() {
            AbstractRelic relic = AbstractDungeon.player.getRelic(PuritySeal.ID);
            if (relic == null) {
                return;
            }

            relic.flash();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ZealPower(AbstractDungeon.player, ZEAL_AMOUNT)));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
        }
    }
}
