<h1> Simple command line crypto chart </h1>

<p align="center">
   <img src="https://github.com/owpk//blob/master/img/g.gif"/>
</p>

- Build
```bash
$ ./gradlew clean build
```

- Run
use gradle wrapper

```bash
$ ./gradlew run

# by default used BTC-USD pair but you can pass any with option -p (--pair)
$ ./gradlew run --args='-p BTC-ETH'
```

<h3> websocket resource: </h3>
https://docs.pro.coinbase.com/#the-status-channel

<h3> dependencies: </h3>
ASCII chart: https://github.com/MitchTalmadge/ASCII-Data
Websocket client: https://github.com/jetty-project

<h3> Useful docs: </h3>
jetty wsclient https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-websocket-client-api
