package morerelics.relics;

import java.util.ArrayList;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import morerelics.MoreRelics;
import morerelics.campfireOptions.BottomlessFlaskOption;

public class BottomlessFlask extends BaseRelic {
    public static final String ID = MoreRelics.makeID("BottomlessFlask");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.CLINK;
    public static final int NUM_TO_BREW = 3;

    public BottomlessFlask() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new BottomlessFlask();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + NUM_TO_BREW + DESCRIPTIONS[1];
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new BottomlessFlaskOption());
    }
}
