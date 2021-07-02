package org.owpk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchtalmadge.asciidata.graph.ASCIIGraph;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@Setter
public class CliView implements View<String> {
    private static final Integer X_AXIS = 60;
    private static final Integer Y_AXIS = 15;
    private static final Integer TIMER_DELAY = 500;
    private static final Integer TEMP_DATA_THRESHOLD = 10;
    private final ObjectMapper objectMapper;
    private final Queue<Double> chartData;
    private final Queue<Double> tempChartData;
    private EventSocket eventSocket;
    private Integer yAxis;
    private Integer xAxis;
    private Integer timerDelay;
    private int timer;

    public CliView() {
        this(X_AXIS, Y_AXIS, TIMER_DELAY);
    }

    public CliView(Integer x, Integer y, Integer timerDelay) {
        this.yAxis = y;
        this.xAxis = x;
        this.timerDelay = timerDelay;
        this.eventSocket = new EventSocket(this, new MessageProvider());
        this.objectMapper = new ObjectMapper();
        this.chartData = new ArrayDeque<>();
        this.tempChartData = new ArrayDeque<>();
    }

    @Override
    public EventSocket getEventHandler() {
        return eventSocket;
    }

    @Override
    public void setEventHandler(EventSocket socket) {
        this.eventSocket = socket;
    }

    @Override
    public void handleData(String data) {
        try {
            var response = (CoinBaseResponse) objectMapper.readValue(data, CoinBaseResponse.class);
            double val = Double.parseDouble(response.getPrice());
            tempChartData.add(val);
            if (tempChartData.size() > TEMP_DATA_THRESHOLD)
                tempChartData.poll();
            if (timer == 0 || timer > timerDelay) {
                if (timer > 0)
                    clear();
                render(tempChartData.stream()
                        .reduce(0d, Double::sum) / tempChartData.size());
                timer = 0;
            }
            timer++;
        } catch (Throwable e) {
            // ignore
            // e.printStackTrace();
        }
    }

    private void render(Double data) {
        if (chartData.size() > xAxis)
            chartData.poll();
        chartData.add(data);
        double[] arr = chartData.stream().mapToDouble(Double::doubleValue).toArray();
        System.out.println(ASCIIGraph.fromSeries(arr).withNumRows(yAxis).plot());
    }

    private void clear() {
        for (int i = 0; i < yAxis + 1; i++) {
            System.out.printf("\033[%dA", 1);
            System.out.print("\033[2K");
        }
    }

    @Override
    public void handleError(Throwable data) {
        data.printStackTrace(System.out);
    }

}
