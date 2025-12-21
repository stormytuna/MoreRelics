package morerelics.powers;

import java.util.ArrayList;

import org.clapper.util.logging.LogLevel;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.finders.MatchFinderExprEditor;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import morerelics.MoreRelics;

public class ZealPower extends BasePower {
    public static final String ID = MoreRelics.makeID("ZealPower");
    public static final PowerType TYPE = PowerType.BUFF;
    public static final boolean TURN_BASED = false;

    public ZealPower(AbstractCreature owner, int amount) {
        super(ID, TYPE, TURN_BASED, owner, amount);
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        } 
    }

    public void onSpecificTrigger() {
        if (this.amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ID));
        } else {
            addToTop(new ReducePowerAction(this.owner, this.owner, ID, 1));
        } 
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class RemoveBuffsPatch {
        @SpireInsertPatch(rloc = 26)
        public static SpireReturn<Void> patch(ApplyPowerAction __instance, AbstractPower ___powerToApply, @ByRef float[] ___duration) {
            if (__instance.target.hasPower(ZealPower.ID) && ___powerToApply.type == AbstractPower.PowerType.BUFF) {
                AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(__instance.target, ApplyPowerAction.TEXT[0]));
                ___duration[0] -= Gdx.graphics.getDeltaTime();
                __instance.target.getPower(ZealPower.ID).flashWithoutSound();
                __instance.target.getPower(ZealPower.ID).onSpecificTrigger();

                return SpireReturn.Return(); 
            }

            return SpireReturn.Continue(); 
        }
    }
}
