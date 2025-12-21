package morerelics.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class TrainingWheels extends BaseRelic {
    public static final String ID = MoreRelics.makeID("TrainingWheels");
    public static final RelicTier TIER = RelicTier.BOSS;
    public static final LandingSound LANDING_SOUND = LandingSound.HEAVY; // TODO: Custom egg cracking sound

    public TrainingWheels() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new TrainingWheels();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void update() {
        super.update();

        if (AbstractDungeon.player ==  null) {
            return;
        }

        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            card.setCostForTurn(1);
        }
    }
}
