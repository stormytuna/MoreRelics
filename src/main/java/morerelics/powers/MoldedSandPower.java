package morerelics.powers;

import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;

import morerelics.MoreRelics;

public class MoldedSandPower extends BasePower {
    public static final String ID = MoreRelics.makeID("MoldedSandPower");
    public static final PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public MoldedSandPower(AbstractCreature owner) {
        super(ID, TYPE, TURN_BASED, owner, -1);
    }

    public float atDamageFinalGive(float damage, DamageType type) {
        if (type == DamageType.NORMAL) {
            return damage * 2;
        }

        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    public float atDamageFinalReceive(float damage, DamageType type) {
        if (type == DamageType.NORMAL) {
            return damage * 2;
        }

        return damage;
    }
}
