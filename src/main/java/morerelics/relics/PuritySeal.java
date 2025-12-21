package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

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

    public void atBattleStart() {
        flash();
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ZealPower(AbstractDungeon.player, ZEAL_AMOUNT)));
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    } 
}
