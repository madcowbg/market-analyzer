package view.sett.ui.minimap;

import init.resources.RESOURCE;
import profitability.CalculationUtils;
import profitability.RateCalculator;
import settlement.main.SETT;
import settlement.room.industry.module.Industry;
import util.gui.misc.GBox;
import util.info.GFORMAT;

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

    static void drawPossibleRecipes(GBox b, RESOURCE res) {
        for (Industry recipe : SETT.ROOMS().INDUSTRIES) {
            b.sep();

            if (ProducerUtils.hasOutput(recipe, res)) {
                b.text("Recipe from " + recipe.blue.key);
                b.NL();

                String recipeDesc = "";
                for (Industry.IndustryResource resourceIn : recipe.ins()) {
                    recipeDesc += resourceIn.rate + "x " + resourceIn.resource.name + " ";
                }

                recipeDesc += " -> ";
                for (Industry.IndustryResource resourceOut : recipe.outs()) {
                    recipeDesc += resourceOut.rate + "x " + resourceOut.resource.name;
                }
                b.tab(1);
                b.text(recipeDesc);
                b.NL();

                b.tab(0);
                b.textLL("Value added");

                b.tab(3);
                b.textLL("Trade profit");

                b.tab(6);
                b.textLL("Consumption");
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
                b.NL();
            }
        }
    }
}
