package morerelics.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import morerelics.MoreRelics;

public class LineGraph extends BaseRelic {
    public static final String ID = MoreRelics.makeID("LineGraph");
    public static final RelicTier TIER = RelicTier.SHOP;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    public static final float INTEREST_AMOUNT = 0.02f;

    public LineGraph() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new LineGraph();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + String.format("%.0f%%", INTEREST_AMOUNT * 100) + DESCRIPTIONS[1];
    }

    public void onEnterRoom(AbstractRoom room) {
        flash();
        AbstractDungeon.player.gainGold((int)(AbstractDungeon.player.gold * INTEREST_AMOUNT));
    }
}
