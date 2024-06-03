package profitability;

import settlement.room.industry.module.Industry;

public interface RateCalculator {
    double productionRate(Industry.IndustryResource i);

    double consumptionRate(Industry.IndustryResource i);
}
