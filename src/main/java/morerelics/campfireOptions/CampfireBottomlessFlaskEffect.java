package morerelics.campfireOptions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import morerelics.relics.BottomlessFlask;

public class CampfireBottomlessFlaskEffect extends AbstractGameEffect {
    private Color screenColor = AbstractDungeon.fadeColor.cpy();
    private boolean hasBrewed = false;

    public CampfireBottomlessFlaskEffect() {
        duration = 2.0f;
        screenColor.a = 0f;
        ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
    }

    public void update() {
        duration -= Gdx.graphics.getDeltaTime();
        
        if (duration > 1.5f) {
            screenColor.a = Interpolation.fade.apply(1f, 0f, (duration - 1.5f) * 2f);
        } else if (duration < 1f) {
            screenColor.a = Interpolation.fade.apply(0f, 1f, duration);
        } else {
            screenColor.a = 1f;
        }

        if (duration < 1f && !hasBrewed) {
            hasBrewed = true;
            // TODO: Sound?
            AbstractRoom room = AbstractDungeon.getCurrRoom();

            room.rewards.clear();
            for (int i = 0; i < BottomlessFlask.NUM_TO_BREW; i++) {
                room.addPotionToRewards(PotionHelper.getRandomPotion());
            }

            room.phase = RoomPhase.COMPLETE;
            AbstractDungeon.combatRewardScreen.open();
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, Settings.WIDTH, Settings.HEIGHT);
    }

    public void dispose() {}
}
