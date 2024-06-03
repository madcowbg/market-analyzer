package view.sett.ui.minimap;

import init.resources.RESOURCE;
import settlement.room.industry.module.Industry;

public class ProducerUtils {
    public static boolean hasOutput(Industry recipe, RESOURCE res) {
        for (Industry.IndustryResource outRes : recipe.outs()) {
            if (res == outRes.resource) {
                return true;
            }
        }
        return false;
    }
}
