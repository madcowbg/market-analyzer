package profitability.industry;

import init.resources.RESOURCE;
import init.sprite.SPRITES;
import profitability.CalculationUtils;
import profitability.RateCalculator;
import settlement.room.industry.module.Industry;
import util.gui.misc.GBox;
import util.info.GFORMAT;

import java.util.*;

public class RecipeAnalysisUI {
    static RateCalculator rateCalculator = new RateCalculator() {

        @Override
        public double productionRate(Industry.IndustryResource i) {
            return i.rate;
        }

        @Override
        public double consumptionRate(Industry.IndustryResource i) {
            return i.rate;
        }
    };

    public static void drawPossibleRecipes(GBox b, RESOURCE res) {
        for (Industry recipe : ProducerUtils.RESOURCE_PRODUCERS().getOrDefault(res, Collections.emptyList())) {
            b.sep();

            b.add(recipe.blue.icon);
            b.text(recipe.blue.info.name);
            b.tab(5);

            for (Industry.IndustryResource resourceIn : recipe.ins()) {
                b.text("" + resourceIn.rate);
                b.add(resourceIn.resource.icon());
            }

            b.add(SPRITES.icons().m.arrow_right);
            for (Industry.IndustryResource resourceOut : recipe.outs()) {
                b.text("" + resourceOut.rate);
                b.add(resourceOut.resource.icon());
            }
            b.NL();

            b.tab(0);
            b.textLL("Value added");

            b.tab(3);
            b.textLL("Trade profit");

            b.tab(6);
            b.textLL("Consumption");

            b.tab(9);
            b.textLL("Trade instead");

            b.NL();

            b.tab(1);
            b.text(GFORMAT.f(b.text(), CalculationUtils.getValueAdded(
                    recipe,
                    rateCalculator), 1));

            b.tab(4);
            b.text(GFORMAT.f(b.text(), CalculationUtils.getTradeProfit(
                    recipe,
                    rateCalculator), 1));

            b.tab(7);
            b.text(GFORMAT.f(b.text(), CalculationUtils.getConsumptionProfit(
                    recipe,
                    rateCalculator), 1));

            b.tab(10);
            b.text(GFORMAT.f(b.text(), CalculationUtils.getTradeInstead(
                    recipe,
                    rateCalculator), 1));
            b.NL();
        }
    }
}
