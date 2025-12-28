package morerelics.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.SmithOption;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;

import morerelics.MoreRelics;

public class AdamantineHammer extends BaseRelic {
    public static final String ID = MoreRelics.makeID("AdamantineHammer");
    public static final RelicTier TIER = RelicTier.RARE;
    public static final LandingSound LANDING_SOUND = LandingSound.HEAVY;

    public AdamantineHammer() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new AdamantineHammer();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEnterRestRoom() {
        flash();
        beginLongPulse();
    }

    public void justEnteredRoom(AbstractRoom room) {
        pulse = false;
    }

    @SpirePatch(clz = SmithOption.class, method = "useOption") 
    public static class AllowDoubleSmithingPatch {
        @SpirePostfixPatch
        public static void patch(SmithOption __instance) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(AdamantineHammer.ID);
            if (relic != null && AbstractDungeon.player.masterDeck.getUpgradableCards().size() > 1 && __instance.usable) {
                AbstractDungeon.effectList.add(new CampfireSmithEffect());
            }
        }
    }
}
