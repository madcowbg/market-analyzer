package profitability;

import game.faction.FACTIONS;
import settlement.room.industry.module.Industry;

public class CalculationUtils {
    public static Pricing TRADE_BUY_PRICE = resource -> FACTIONS.player().trade.pricesBuy.get(resource);
    public static Pricing TRADE_SELL_PRICE = resource -> FACTIONS.player().trade.pricesSell.get(resource);


    public static double priceOutputResource(Industry.IndustryResource i, RateCalculator rateCalculator, Pricing pricing) {
        double n = rateCalculator.productionRate(i);
        double buyPrice = pricing.price(i.resource);

        return (n * buyPrice);
    }

    public static double priceInputResource(Industry.IndustryResource i, RateCalculator rateCalculator, Pricing pricing) {
        double n = rateCalculator.consumptionRate(i);
        double buyPrice = pricing.price(i.resource);

        return (n * buyPrice);
    }

    public static double getExpenses(Industry industry, RateCalculator rateCalculator, Pricing pricing){
        double costs = 0;
        for (Industry.IndustryResource res: industry.ins()) {
            costs += CalculationUtils.priceInputResource(res, rateCalculator, pricing);
        }
        return costs;
    }

    public static double getIncomes(Industry industry, RateCalculator rateCalculator, Pricing pricing){
        double costs = 0;
        for (Industry.IndustryResource res: industry.outs()) {
            costs += CalculationUtils.priceOutputResource(res, rateCalculator, pricing);
        }
        return costs;
    }

    public static double getTradeProfit(Industry industry, RateCalculator rateCalculator) {
        return getIncomes(industry, rateCalculator, TRADE_SELL_PRICE) - getExpenses(industry, rateCalculator, TRADE_BUY_PRICE);
    }

    public static double getValueAdded(Industry industry, RateCalculator rateCalculator) {
        return getIncomes(industry, rateCalculator, TRADE_SELL_PRICE) - getExpenses(industry, rateCalculator, TRADE_SELL_PRICE);
    }

    public static double getConsumptionProfit(Industry industry, RateCalculator rateCalculator) {
        return getIncomes(industry, rateCalculator, TRADE_BUY_PRICE) - getExpenses(industry, rateCalculator, TRADE_BUY_PRICE);
    }
}
