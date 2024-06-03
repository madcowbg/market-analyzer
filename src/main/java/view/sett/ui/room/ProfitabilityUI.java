package view.sett.ui.room;

import game.faction.FACTIONS;
import profitability.CalculationUtils;
import profitability.Pricing;
import profitability.RoomInstanceRateCalculator;
import settlement.room.industry.module.Industry;
import settlement.room.industry.module.IndustryUtil;
import settlement.room.industry.module.ROOM_PRODUCER;
import settlement.room.main.RoomInstance;
import snake2d.util.datatypes.DIR;
import snake2d.util.gui.GUI_BOX;
import snake2d.util.gui.GuiSection;
import snake2d.util.gui.Hoverable.HOVERABLE;
import snake2d.util.sets.LIST;
import util.data.GETTER;
import util.gui.misc.GBox;
import util.gui.misc.GHeader;
import util.gui.misc.GStat;
import util.gui.misc.GText;
import util.info.GFORMAT;

import java.util.function.Function;

import static profitability.CalculationUtils.TRADE_BUY_PRICE;
import static profitability.CalculationUtils.TRADE_SELL_PRICE;

public class ProfitabilityUI {
    public static void drawProfitabilitySection(GuiSection section, GETTER<RoomInstance> get) {
        String ¤¤Profitability = "Estimated Profit";

        GuiSection all = new GuiSection();

        all.addRelBody(4, DIR.S, drawValueAddedSection(get));
        all.addRelBody(4, DIR.S, drawTradeProfitSection(get));
        all.addRelBody(4, DIR.S, drawConsumptionProfitSection(get));

        all.addRelBody(2, DIR.N, new GHeader(¤¤Profitability));

        all.body().incrW(48);
        all.pad(4);

        section.addRelBody(4, DIR.S, all);
    }

    private static GuiSection drawValueAddedSection(GETTER<RoomInstance> get) {
        GuiSection valueAddedSection = new GuiSection() {
            @Override
            public void hoverInfoGet(GUI_BOX text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                RoomInstanceRateCalculator rateCalculator = new RoomInstanceRateCalculator(get.get());

                GBox b = (GBox) text;

                b.title("Value Added");

                b.text("Calculated added value (export output vs export ingredient) per employee, based on the rate and bonuses applied to this room.");
                b.NL(8);

                drawResourceSplit(rateCalculator::consumptionRate, TRADE_SELL_PRICE, roomProducer.industry().ins(), b);
                b.textLL("Export profit of ingredients");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getExpenses(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_SELL_PRICE)));
                b.NL(8);

                drawResourceSplit(rateCalculator::productionRate, TRADE_SELL_PRICE, roomProducer.industry().outs(), b);
                b.textLL("Export profits of outputs");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getIncomes(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_SELL_PRICE)));
                b.NL(8);

                b.textLL("Value added");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getValueAdded(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()))));

                b.NL(8);
            }
        };

        valueAddedSection.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("Value added: ¤");
                GFORMAT.f(text, CalculationUtils.getValueAdded(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get())), 0);
            }
        }.r());

        valueAddedSection.body().incrW(48);
        valueAddedSection.pad(4);
        return valueAddedSection;
    }

    private static GuiSection drawTradeProfitSection(GETTER<RoomInstance> get) {
        GuiSection tradeProfitSection = new GuiSection() {
            @Override
            public void hoverInfoGet(GUI_BOX text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                RoomInstanceRateCalculator rateCalculator = new RoomInstanceRateCalculator(get.get());

                GBox b = (GBox) text;

                b.title("Export Profitability");

                b.text("Calculated profitability if the ingredients are imported and the outputs are exported, per employee, based on the rate and bonuses applied to this room.");
                b.NL(8);

                drawResourceSplit(rateCalculator::consumptionRate, TRADE_BUY_PRICE, roomProducer.industry().ins(), b);

                b.textLL("Import cost of ingredients");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getExpenses(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_BUY_PRICE)));
                b.NL(8);

                drawResourceSplit(rateCalculator::productionRate, TRADE_SELL_PRICE, roomProducer.industry().outs(), b);
                b.textLL("Export profits of outputs");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getIncomes(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_SELL_PRICE)));
                b.NL(8);

                b.textLL("Trade profits");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getTradeProfit(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()))));

                b.NL(8);
            }
        };

        tradeProfitSection.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("Trade profit: ¤");
                GFORMAT.f(text, CalculationUtils.getTradeProfit(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get())), 0);
            }
        }.r());

        tradeProfitSection.body().incrW(48);
        tradeProfitSection.pad(4);
        return tradeProfitSection;
    }

    private static void drawResourceSplit(Function<Industry.IndustryResource, Double> rate, Pricing pricing, LIST<Industry.IndustryResource> resources, GBox b) {
        for (Industry.IndustryResource res: resources) {
            double n = rate.apply(res);
            double buyPrice = pricing.price(res.resource);

            b.tab(1);
            b.textLL(res.resource.name);

            b.tab(4);
            b.text(GFORMAT.f(b.text(), n) + " x " + GFORMAT.f(b.text(), buyPrice, 0) + " = ");

            b.tab(7);
            b.text(GFORMAT.f(b.text(), n * buyPrice));
            b.NL();
        }
    }

    private static GuiSection drawConsumptionProfitSection(GETTER<RoomInstance> get) {
        GuiSection tradeProfitSection = new GuiSection() {
            @Override
            public void hoverInfoGet(GUI_BOX text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                RoomInstanceRateCalculator rateCalculator = new RoomInstanceRateCalculator(get.get());

                GBox b = (GBox) text;

                b.title("Consumption Profitability");

                b.text("Calculated profitability of consumption if the ingredients are imported instead of the outputs, per employee, based on the rate and bonuses applied to this room.");
                b.NL(8);

                drawResourceSplit(rateCalculator::consumptionRate, TRADE_BUY_PRICE, roomProducer.industry().ins(), b);
                b.textLL("Import cost of ingredients");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getExpenses(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_BUY_PRICE)));
                b.NL(8);

                drawResourceSplit(rateCalculator::productionRate, TRADE_BUY_PRICE, roomProducer.industry().outs(), b);
                b.textLL("Use cost of outputs");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getIncomes(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()), TRADE_BUY_PRICE)));
                b.NL(0);

                b.textLL("Trade profits");
                b.tab(7);
                b.add(GFORMAT.f(b.text(), CalculationUtils.getConsumptionProfit(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get()))));

                b.NL(8);
            }
        };

        tradeProfitSection.add(new GStat() {
            @Override
            public void update(GText text) {
                ROOM_PRODUCER roomProducer = (ROOM_PRODUCER) get.get();
                text.add("Consumption: ¤");
                GFORMAT.f(text, CalculationUtils.getConsumptionProfit(
                        roomProducer.industry(),
                        new RoomInstanceRateCalculator(get.get())), 0);
            }
        }.r());

        tradeProfitSection.body().incrW(48);
        tradeProfitSection.pad(4);
        return tradeProfitSection;
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
