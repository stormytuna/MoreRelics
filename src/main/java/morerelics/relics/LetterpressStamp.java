package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import morerelics.MoreRelics;

public class LetterpressStamp extends BaseRelic {
    public static final String ID = MoreRelics.makeID("LetterpressStamp");
    public static final RelicTier TIER = RelicTier.UNCOMMON;
    public static final LandingSound LANDING_SOUND = LandingSound.FLAT; // TODO: Custom sound
    public static final float INTEREST_AMOUNT = 0.02f;

    private boolean doubledCardThisCombat = true;

    public LetterpressStamp() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new LetterpressStamp();
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (!card.purgeOnUse && !doubledCardThisCombat) {
            doubledCardThisCombat = true;

            this.pulse = false;
            grayscale = true;
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            AbstractCard temp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(temp);
            temp.current_x = card.current_x;
            temp.current_y = card.current_y;
            temp.target_x = (float)Settings.WIDTH / 2 - 300 * Settings.scale;
            temp.target_y = (float)Settings.HEIGHT / 2;
            temp.purgeOnUse = true;

            if (monster != null) {
                temp.calculateCardDamage(monster);
            }

            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(temp, monster, card.energyOnUse, true, true), true);
        }
    }

    public void onEnterRoom(AbstractRoom room) {
        doubledCardThisCombat = false;
        flash();
        beginLongPulse();
        grayscale = false;
    }
}
