package profitability;

import game.faction.FACTIONS;
import settlement.room.industry.module.Industry;
import settlement.room.industry.module.ROOM_PRODUCER;
import settlement.room.main.RoomInstance;
import snake2d.util.sets.LIST;

public class CalculationUtils {

    public static double getBuyPriceEquivalentIncomePP(Industry.IndustryResource i, RateCalculator rateCalculator) {
        double n = rateCalculator.productionRate(i);
        double buyPrice = FACTIONS.player().trade.pricesBuy.get(i.resource);

        return (n * buyPrice);
    }

    public static double getBuyPriceEquivalentExpensePP(Industry.IndustryResource i, RateCalculator rateCalculator) {
        double n = rateCalculator.consumptionRate(i);
        double buyPrice = FACTIONS.player().trade.pricesBuy.get(i.resource);

        return (n * buyPrice);
    }

    public static double getExpensesPP(RoomInstance ins){
        RoomInstanceRateCalculator rateCalculator = new RoomInstanceRateCalculator(ins);
        LIST<Industry.IndustryResource> resources = ((ROOM_PRODUCER) ins).industry().ins();
        double costs = 0;
        for (Industry.IndustryResource res: resources) {
            costs += CalculationUtils.getBuyPriceEquivalentExpensePP(res, rateCalculator);
        }
        return costs;
    }

    public static double getIncomesPP(RoomInstance ins){
        RoomInstanceRateCalculator rateCalculator = new RoomInstanceRateCalculator(ins);
        LIST<Industry.IndustryResource> resources = ((ROOM_PRODUCER) ins).industry().outs();
        double costs = 0;
        for (Industry.IndustryResource res: resources) {
            costs += CalculationUtils.getBuyPriceEquivalentIncomePP(res, rateCalculator);
        }
        return costs;
    }

    public static double getProfitPP(RoomInstance roomInstance) {
        return getIncomesPP(roomInstance) - getExpensesPP(roomInstance);
    }
}
