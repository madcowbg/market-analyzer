package profitability;

import settlement.room.industry.module.Industry;
import settlement.room.industry.module.IndustryUtil;
import settlement.room.industry.module.ROOM_PRODUCER;
import settlement.room.main.RoomInstance;

public class RoomInstanceRateCalculator implements RateCalculator {
    final RoomInstance ins;

    public RoomInstanceRateCalculator(RoomInstance ins) {
        this.ins = ins;
    }

    @Override
    public double productionRate(Industry.IndustryResource i) {
        return IndustryUtil.calcProductionRate(i.rate, ((ROOM_PRODUCER) ins).industry(), ins);
    }

    @Override
    public double consumptionRate(Industry.IndustryResource i) {
        return IndustryUtil.calcConsumptionRate(i.rate, ((ROOM_PRODUCER) ins).industry(), ins, i.resource);
    }
}
