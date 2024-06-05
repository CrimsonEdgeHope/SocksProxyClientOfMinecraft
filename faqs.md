# FAQs

## 1.x.x (major version) Forge/Fabric/Quilt/NeoForge please?

Currently, this mod sticks to Fabric.

## Fabric API required?

This mod doesn't declare Fabric API as a dependency, so it's a No. But you better have it.

## Fabric server applicable?

No. This mod is client-side.

## Compatibility with (mod name) mod?

Depends. Should be compatible as long as (mod name) doesn't mess up with the part where Minecraft handles network connection.

## Will this mod ever be allowed on (server name)?

Look, you, as a simple Minecraft Java player, just want a nice game playing (fairness of course)
or have privacy protected, such desire is 100% acceptable.
You don't want to play on a server under 400+ ping through a shitty ISP network route.

Also, all code is open source, take a look in it if concerned.
Besides, having a VPN yeets out this mod because your VPN software will take care of every bit of traffic for you.

Still unsure, consult server admin then.
If, unfortunately, (server name) explicitly forbids proxy (or it's pay-to-use-proxy), then issue stands at their side.

## How to see if this mod is actually working?

It will print logs when:

- Pinging a Minecraft server.
- Joining and playing on a Minecraft server.
- Game client makes communications with HTTP(S) servers, including:
  Mojang Yggdrasil auth service, player skin download endpoint, server resource pack downloading, DoH servers.

So what will those logs look like?

Communications with **Minecraft servers** will look like this:
```text
[00:00:00] [Netty Client IO #1/INFO] (SocksProxyClient/Connect) [Socks 5] 127.0.0.1:1080 -> [Remote] mc.hypixel.net:25565
[00:00:00] [Netty Client IO #2/INFO] (SocksProxyClient/Connect) [Direct] -> [Remote] 127.0.0.1:25565
```

Communications with **HTTP(S) servers** will look like this:

```text
[00:00:00] [nioEventLoopGroup-6-1/INFO] (SocksProxyClient/Connect) [Socks 5] 127.0.0.1:1080 -> [Remote] sessionserver.mojang.com:443
[00:00:00] [nioEventLoopGroup-6-1/INFO] (SocksProxyClient/HttpProxy) Open tunnel to remote sessionserver.mojang.com:443
[00:00:02] [nioEventLoopGroup-6-1/INFO] (SocksProxyClient/HttpProxy) Tunnel to sessionserver.mojang.com:443 closed.
```

In addition, when DNS resolve by this mod for Minecraft server engages, there will be logs similar to this:

```text
[00:00:00] [nioEventLoopGroup-8-1/INFO] (SocksProxyClient/Connect) [Socks 5] 127.0.0.1:1080 -> [Remote] 1.1.1.1:443
[00:00:00] [nioEventLoopGroup-8-1/INFO] (SocksProxyClient/HttpProxy) Open tunnel to remote 1.1.1.1:443
[00:00:03] [Server Pinger #1/INFO] (SocksProxyClient/Resolve) Resolve mc.hypixel.net to 209.222.115.23
[00:00:03] [Netty Client IO #11/INFO] (SocksProxyClient/Connect) [Socks 5] 127.0.0.1:1080 -> [Remote] mc.hypixel.net:25565
```

## Aren't proxyHost and proxyPort (and proxyUser and proxyPass) game parameters sufficient?

No. In fact, they are practically useless. Minecraft will take it as a SOCKS proxy, while JDK's HTTP client wants an HTTP proxy.
The root cause lies in JDK's native implementation.

Therefore, should you take this game-parameter approach, you likely won't pass verification upon joining servers
because your genuine IP address and proxy IP address have 0 chance of matching, unless your desired server allows
proxies.
