package view.sett.ui.room;

import game.faction.FACTIONS;
import profitability.CalculationUtils;
import profitability.RoomInstanceRateCalculator;
import settlement.room.industry.module.Industry;
import settlement.room.industry.module.IndustryUtil;
import settlement.room.industry.module.ROOM_PRODUCER;
import settlement.room.main.RoomInstance;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.Hoverable.HOVERABLE;
import util.data.GETTER;
import util.gui.misc.GHeader;
import util.gui.misc.GStat;
import util.gui.misc.GText;
import util.info.GFORMAT;

import static profitability.CalculationUtils.TRADE_BUY_PRICE;
import static profitability.CalculationUtils.TRADE_SELL_PRICE;

public class ProfitabilityUI {
    public static void drawProfitabilitySection(GuiSection section, GETTER<RoomInstance> get) {
        String ¤¤Profitability = "Profitability";
        GuiSection all = new GuiSection();

        all.addRightC(6, new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("ex: -¤");
                GFORMAT.f(text, CalculationUtils.getExpenses(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_SELL_PRICE), 0);
            }
        }.r());

        all.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("inc: ¤");
                GFORMAT.f(text, CalculationUtils.getIncomes(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_SELL_PRICE), 0);
            }
        }.r(), all.getLastX1(), all.getLastY2() + 1);

        all.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("vadd ¤");
                GFORMAT.f(text, CalculationUtils.getValueAdded(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get())), 0);
            }
        }.r(), all.getLastX1(), all.getLastY2() + 1);

        all.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("prf: ¤");
                GFORMAT.f(text, CalculationUtils.getTradeProfit(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get())), 0);
            }
        }.r(), all.getLastX1(), all.getLastY2() + 1);

        all.addRelBody(2, DIR.N, new GHeader(¤¤Profitability));

        section.addRelBody(4, DIR.S, all);
    }

    public static void drawInputProfitability(int ri, GETTER<RoomInstance> get, GuiSection s) {
        HOVERABLE h;
        // FIXME refactor

        h = new GStat() {

            @Override
            public void update(GText text) {
                ROOM_PRODUCER producer = (ROOM_PRODUCER) get.get();
                Industry.IndustryResource i = producer.industry().ins().get(ri);

                double sellPrice = FACTIONS.player().trade.pricesSell.get(i.resource);
                double n = IndustryUtil.calcConsumptionRate(i.rate, producer.industry(), (RoomInstance) get.get(), i.resource);

                text.add("E-¤");
                GFORMAT.f(text, (n * sellPrice), 1);
            }
        }.r();

        s.add(h, s.getLast().x1(), s.getLastY2()+1);
//		s.addRightC(6, h);

        h = new GStat() {

            @Override
            public void update(GText text) {
                ROOM_PRODUCER producer = (ROOM_PRODUCER) get.get();
                Industry industry = producer.industry();
                Industry.IndustryResource i = industry.ins().get(ri);
                RoomInstance ins = (RoomInstance) get.get();

                double buyPrice = FACTIONS.player().trade.pricesBuy.get(i.resource);

                double n = IndustryUtil.calcConsumptionRate(i.rate, industry, ins, i.resource);

                text.add("I-¤");
                GFORMAT.f(text, (n * buyPrice), 1);
            }
        }.r();

        s.add(h, s.getLast().x1(), s.getLastY2()+1);
//		s.addRightC(6, h);
    }

    public static void drawOutputProfitability(int ri, GETTER<RoomInstance> get, GuiSection s) {
        HOVERABLE h;
        // FIXME refactor

        h = new GStat() {

            @Override
            public void update(GText text) {
                RoomInstance ins = (RoomInstance) get.get();
                Industry.IndustryResource i = ((ROOM_PRODUCER) ins).industry().outs().get(ri);

                double buyPriceEquivalentPP = CalculationUtils.priceInputResource(i, new RoomInstanceRateCalculator(ins), TRADE_BUY_PRICE);

                text.add("I¤");
                GFORMAT.f(text, buyPriceEquivalentPP, 1);
//				GFORMAT.fRel(text, n*ins.employees().efficiency(), n);
            }
        }.r();

        s.add(h, s.getLast().x1(), s.getLastY2()+1);

        h = new GStat() {

            @Override
            public void update(GText text) {
                ROOM_PRODUCER producer = (ROOM_PRODUCER) get.get();
                Industry.IndustryResource i = producer.industry().outs().get(ri);
                double sellPrice = FACTIONS.player().trade.pricesSell.get(i.resource);
                double n = IndustryUtil.calcProductionRate(i.rate, producer.industry(), get.get());

                text.add("E¤");
                GFORMAT.f(text, (n * sellPrice), 1);
            }
        }.r();

        s.add(h, s.getLast().x1(), s.getLastY2()+1);
    }
}
