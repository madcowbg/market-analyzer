package view.sett.ui.minimap;

import init.resources.RESOURCE;
import settlement.main.SETT;
import settlement.room.industry.module.Industry;
import util.gui.misc.GBox;

public class RecipeAnalysisUI {
    static void drawPossibleRecipes(GBox b, RESOURCE res) {
        // FIXME refactor
        b.sep();

        for (Industry recipe : SETT.ROOMS().INDUSTRIES) {
            if (ProducerUtils.hasOutput(recipe, res)) {
                b.text("Recipe from " + recipe.blue.key);
                b.NL();

                for (Industry.IndustryResource resourceIn : recipe.ins()) {
                    b.text("IN " + resourceIn.rate + " x " + resourceIn.resource);
                    b.NL();
                }

                for (Industry.IndustryResource resourceOut : recipe.outs()) {
                    b.text("OUT " + resourceOut.rate + " x " + resourceOut.resource);
                    b.NL();
                }
            }
        }
    }
}
