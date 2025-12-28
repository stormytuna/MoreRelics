package morerelics.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import morerelics.MoreRelics;

public class TheNeedle extends BaseRelic {
    public static final String ID = MoreRelics.makeID("TheNeedle");
    public static final RelicTier TIER = RelicTier.SHOP;
    public static final LandingSound LANDING_SOUND = LandingSound.CLINK;

    public TheNeedle() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new TheNeedle();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (card.type == CardType.ATTACK) {
            AbstractPower dexterity = AbstractDungeon.player.getPower("Dexterity");
            if (dexterity == null || dexterity.amount < 0) {
                return;
            }

            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1)));
            dexterity.stackPower(-1);
        }
        else if (card.type == CardType.SKILL) {
            AbstractPower strength = AbstractDungeon.player.getPower("Strength");
            if (strength == null || strength.amount < 0) {
                return;
            }

            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1)));
            strength.stackPower(-1);
        }
    }
}
