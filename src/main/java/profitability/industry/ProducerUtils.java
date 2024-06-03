package profitability.industry;

import init.resources.RESOURCE;
import settlement.main.SETT;
import settlement.room.industry.module.Industry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProducerUtils {
    public static Map<RESOURCE, List<Industry>> RESOURCE_PRODUCERS() {
        Map<RESOURCE, List<Industry>> result = new HashMap<>();
        for (Industry recipe : SETT.ROOMS().INDUSTRIES) {
            for (Industry.IndustryResource outResource : recipe.outs()) {
                result.computeIfAbsent(outResource.resource, (r) -> new ArrayList<>()).add(recipe);
            }
        }
        return result;
    }
}
