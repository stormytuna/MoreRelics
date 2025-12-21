package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import morerelics.MoreRelics;

public class TinBracelet extends BaseRelic {
    public static final String ID = MoreRelics.makeID("TinBracelet");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.CLINK; // TODO: Custom sound maybe

    private static boolean canGainEnergy = true;

    public TinBracelet() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new TinBracelet();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }

    @SpirePatch(clz = GainEnergyAndEnableControlsAction.class, method = "update")
    public static class DisableRelicForStartingEnergyPatch {
        @SpirePrefixPatch
        public static void patch(GainEnergyAndEnableControlsAction __instance, float ___duration) {
            if  (___duration == 0.5f) {
                canGainEnergy = true;
            }
        }

        @SpirePostfixPatch
        public static void patch() {
            canGainEnergy = false;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "gainEnergy")
    public static class UndoGainEnergyPatch {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance, @ByRef int[] e) {
            AbstractRelic relic = __instance.getRelic(TinBracelet.ID);
            if (relic != null && !canGainEnergy) {
                e[0] = 0;
                relic.flash();
            }
        }
    }

    @SpirePatch(clz = GameActionManager.class, method = "updateEnergyGain") 
    public static class UndoGainEnergyPatch2 {
        @SpirePrefixPatch
        public static void patch(GameActionManager __instance, @ByRef int[] energyGain) {
            if (AbstractDungeon.player.getRelic(TinBracelet.ID) != null && !canGainEnergy) {
                energyGain[0] = 0;
            }
        }
    }
}
