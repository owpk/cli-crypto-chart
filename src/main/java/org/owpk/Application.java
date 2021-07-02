package org.owpk;

import picocli.CommandLine;

@CommandLine.Command(name = "cli-crypto-chart")
public class Application implements Runnable {
    private View<String> view;

    @CommandLine.Option(names = {"-p", "--pair"})
    private String currencyPair;

    public static void main(String[] args) {
        new CommandLine(Application.class).execute(args);
    }

    @Override
    public void run() {
        view = new CliView();
        if (currencyPair != null) {
            view.getEventHandler().setMessageProvider(
                    new MessageProvider(){
                @Override
                public String getMessage() {
                    return "{\"type\": \"subscribe\",\"product_ids\": [\"" + currencyPair
                            + "\"],\"channels\": [\"full\"]}";
                }
            });
        }
        var wsCtx = new WebSocketClientContext("wss://ws-feed.pro.coinbase.com", view);
        wsCtx.connect();
    }
}
