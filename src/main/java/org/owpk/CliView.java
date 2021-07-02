package org.owpk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchtalmadge.asciidata.graph.ASCIIGraph;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class CliView implements View<String> {
    private static final Integer Y_AXIS = 60;
    private static final Integer X_AXIS = 15;
    private static final Integer TIMER_DELAY = 500;
    private final ObjectMapper objectMapper;
    private final Queue<Double> chartData;
    private EventSocket eventSocket;
    private int timer;

    public CliView() {
        this.eventSocket = new EventSocket(this, new MessageProvider());
        this.objectMapper = new ObjectMapper();
        this.chartData = new ArrayDeque<>();
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
        if (timer == 0 || timer > TIMER_DELAY) {
            try {
                if (timer > 0)
                    clear();
                render(data);
            } catch (Throwable e) {
                // ignore
//                e.printStackTrace();
            }
            timer = 0;
        }
        timer++;
    }

    private void render(String data) throws IOException {
        var response = (CoinBaseResponse) objectMapper.readValue(data, CoinBaseResponse.class);
        double val = Double.parseDouble(response.getPrice());
        if (chartData.size() > Y_AXIS)
            chartData.poll();
        chartData.add(val);
        double[] arr = chartData.stream().mapToDouble(Double::doubleValue).toArray();
        System.out.println(ASCIIGraph.fromSeries(arr).withNumRows(X_AXIS).plot());
    }

    private void clear() {
        for (int i = 0; i < X_AXIS + 1; i++) {
            System.out.printf("\033[%dA", 1);
            System.out.print("\033[2K");
        }
    }

    @Override
    public void handleError(Throwable data) {
        data.printStackTrace(System.out);
    }

}
