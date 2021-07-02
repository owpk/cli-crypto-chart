<h1> Simple command line crypto chart </h1>

<p align="center">
   <img src="https://github.com/owpk/cli-crypto-chart/blob/master/docs/ccc.gif"/>
</p>

- Build
```bash
$ ./gradlew clean build
```

- Run</br>
use gradle wrapper

```bash
$ ./gradlew run

# by default used BTC-USD pair but you can pass any with option -p (--pair)
$ ./gradlew run --args='-p BTC-ETH'
```

<h3> websocket resource: </h3>
wss://ws-feed.pro.coinbase.com

<h3> dependencies: </h3>
ASCII chart: https://github.com/MitchTalmadge/ASCII-Data</br>
Websocket client: https://github.com/jetty-project

<h3> useful docs: </h3>
jetty wsclient https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-websocket-client-api
coin base ws provider https://docs.pro.coinbase.com/#the-status-channel
