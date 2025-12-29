package morerelics.relics;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PrismaticShard;

import morerelics.MoreRelics;

public class DullShard extends BaseRelic {
    public static final String ID = MoreRelics.makeID("DullShard");
    public static final RelicTier TIER = RelicTier.SHOP;
    public static final LandingSound LANDING_SOUND = LandingSound.SOLID;

    public DullShard() {
        super(ID, TIER, LANDING_SOUND);
    }

    public AbstractRelic makeCopy() {
        return new DullShard();
    }

    public boolean canSpawn() {
        return MoreRelics.isEnabled(ID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class ApplyDullShardEffect {
        private static CardGroup commonCardPool;
        private static CardGroup uncommonCardPool;
        private static CardGroup rareCardPool;

        @SpireInsertPatch(rloc = 50, localvars = {"card", "rarity"})
        public static void patch(@ByRef AbstractCard[] card, CardRarity rarity) {
            AbstractPlayer player = AbstractDungeon.player;

            if (!player.hasRelic(PrismaticShard.ID) && player.hasRelic(ID)) {
                if (commonCardPool == null || uncommonCardPool == null || rareCardPool == null) {
                    // Setting up card pools the same way that vanilla does
                    ArrayList<AbstractCard> cards = new ArrayList<>();
                    CardLibrary.addColorlessCards(cards);
                    player.getCardPool(cards);

                    commonCardPool = new CardGroup(CardGroupType.UNSPECIFIED);
                    uncommonCardPool = new CardGroup(CardGroupType.UNSPECIFIED);
                    rareCardPool = new CardGroup(CardGroupType.UNSPECIFIED);
                    
                    for (AbstractCard c : cards) {
                        switch (c.rarity) {
                            case COMMON:
                                commonCardPool.addToTop(c);
                                continue;
                            case UNCOMMON:
                                uncommonCardPool.addToTop(c);
                                continue;
                            case RARE:
                                rareCardPool.addToTop(c);
                                continue;
                            default:
                                MoreRelics.logger.info("Skipping card: " + c.name + ", not a common, uncommon, or rare!");
                        }
                    }
                }

                switch (rarity) {
                    case RARE:
                        card[0] = rareCardPool.getRandomCard(true);
                        break;
                    case UNCOMMON:
                        card[0] = uncommonCardPool.getRandomCard(true);
                        break;
                    case COMMON:
                        card[0] = commonCardPool.getRandomCard(true);
                        break;
                    default:
                        MoreRelics.logger.info("How did we get here?");
                }
            }
        }
    }
}
