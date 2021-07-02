package org.owpk;

import picocli.CommandLine;

@CommandLine.Command(name = "cli-crypto-chart")
public class Application implements Runnable {
    private View<String> view;

    @CommandLine.Option(names = {"-p", "--pair"}, description = "crypto pair", defaultValue = "BTC-USD")
    private String currencyPair;

    @CommandLine.Option(names = {"-d", "--delay"}, description = "timeframe delay", defaultValue = "500")
    private int delay;

    @CommandLine.Option(names = {"-x", "--x-axis"}, description = "x axis length", defaultValue = "60")
    private int x;

    @CommandLine.Option(names = {"-y", "--y-axis"}, description = "y axis length", defaultValue = "15")
    private int y;

    public static void main(String[] args) {
        new CommandLine(Application.class).execute(args);
    }

    @Override
    public void run() {
        var msgProvider = new MessageProvider() {
            @Override
            public String getMessage() {
                return "{\"type\": \"subscribe\",\"product_ids\": [\"" + currencyPair
                        + "\"],\"channels\": [\"full\"]}";
            }
        };
        view = new CliView(x, y, delay);
        view.getEventHandler().setMessageProvider(msgProvider);
        var wsCtx = new WebSocketClientContext("wss://ws-feed.pro.coinbase.com", view);
        wsCtx.connect();
    }
}
