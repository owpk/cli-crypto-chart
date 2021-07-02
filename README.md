<h1> Simple command line crypto chart </h1>
<p align="center">
   <img src="https://github.com/owpk//blob/master/img/g.gif"/>
</p>
- Build
```bash
$ gradle clean build
```
- Run
use gradle wrapper
```bash
$ gradle run

# by default used BTC-USD pair but you can pass any with option -p (--pair)
$ gradle run --args='-p BTC-ETH'
```
websocket resource:
https://docs.pro.coinbase.com/#the-status-channel

dependencies:
ASCII chart: https://github.com/MitchTalmadge/ASCII-Data
Websocket client: https://github.com/jetty-project

Usefull docs:
wsclient https://www.eclipse.org/jetty/documentation/jetty-9/index.html#jetty-websocket-client-api
