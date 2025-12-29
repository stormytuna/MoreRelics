package morerelics.ui;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.ModToggleButton;
import basemod.ReflectionHacks;

public class ModLabeledToggleButtonRelicTip extends ModLabeledToggleButton {
    private AbstractRelic relic;

	public ModLabeledToggleButtonRelicTip(AbstractRelic relic, String labelText, float xPos, float yPos, Color color, BitmapFont font, boolean enabled, ModPanel p, Consumer<ModLabel> labelUpdate, Consumer<ModToggleButton> c) {
        super(labelText, null, xPos, yPos, color, font, enabled, p, labelUpdate, c);
        this.relic = relic;
	}

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        Hitbox hitbox = ReflectionHacks.getPrivate(toggle, ModToggleButton.class, "hb");
        if (hitbox.hovered) {
            relic.renderTip(sb); 
        }
    }
}
