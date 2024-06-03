# SocksProxyClient

![Mod loader: Fabric](https://img.shields.io/badge/modloader-Fabric-1976d2?style=for-the-badge&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAACXBIWXMAAAsTAAALEwEAmpwYAAAFHGlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOCAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE4LTEyLTE2VDE2OjU0OjE3LTA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wNy0yOFQyMToxNzo0OC0wNzowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6MGVkYjFjMmMtZmY4Yy1lNDQxLTkzMWYtNDk1ZDRmMTRjNzYwIj4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDowZWRiMWMyYy1mZjhjLWU0NDEtOTMxZi00OTVkNGYxNGM3NjAiIHN0RXZ0OndoZW49IjIwMTgtMTItMTZUMTY6NTQ6MTctMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE4IChXaW5kb3dzKSIvPiA8L3JkZjpTZXE+IDwveG1wTU06SGlzdG9yeT4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4/HiGMAAAAtUlEQVRYw+XXrQqAMBQF4D2P2eBL+QIG8RnEJFaNBjEum+0+zMQLtwwv+wV3ZzhhMDgfJ0wUSinxZUQWgKos1JP/AbD4OneIDyQPwCFniA+EJ4CaXm4TxAXCC0BNHgLhAdAnx9hC8PwGSRtAFVMQjF7cNTWED8B1cgwW20yfJgAvrssAsZ1cB3g/xckAxr6FmCDU5N6f488BrpCQ4rQBJkiMYh4ACmLzwOQF0CExinkCsvw7vgGikl+OotaKRwAAAABJRU5ErkJggg==)
&nbsp;![Environment: Client](https://img.shields.io/badge/environment-client-1976d2?style=for-the-badge)
&nbsp;![Minecraft version: 1.20.1](https://img.shields.io/badge/minecraft%20version-1.20.1-1976d2?style=for-the-badge)

Modern Minecraft Java client relies on Netty to connect to multiplayer servers,
and this simple mod is able to proxy Minecraft server traffic through designated SOCKS proxy as desired.

## Features

- SOCKS proxy support. Explicitly proxy Minecraft server traffic.
- Convert SOCKS proxy to HTTP(S) proxy. Proxy communications with Mojang Yggdrasil auth service, player skin download endpoint, and server resource pack downloading.
- Remotely resolve Minecraft server's domain name through SOCKS proxy with the help of [DNS over HTTPS (DoH)](https://www.rfc-editor.org/rfc/rfc8484)

## When to/Why should use this mod?

- You have a better network route through a proxy and want to lower your ping as much as possible.
- You have trouble accepting server resource pack because it's hosted on a provider that's blocked in your residence.
- You care about your privacy.

## Where/How to get this mod?

There are currently several approaches:

- Clone this repository and compile by executing `./gradlew build` on *nix or `./gradlew.bat build` on Windows.
After successful compilation, navigate to `build/libs` directory under the project root directory.
- Navigate to [Actions section](https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/actions),
open the latest successful workflow run and download artifacts. (Artifacts will eventually expire. Compilation approach is preferred)
- ~~Turn to GitHub releases and download jar there. (Coming soon. Haven't decided mod's versioning.)~~
- ~~Go to Modrinth (Planning. Not live yet.)~~

Choose the jar with "SNAPSHOT" suffix.

### CurseForge?

No plan.

### Modrinth?

Planning.

### (Other third-party Minecraft mod distribution platform)?

Currently, no.

## Dependency

Based on Fabric (Minecraft 1.20.1). Just drop mod jar in mods folder, and voilà!

- In addition, you probably want [Cloth Config API (at least 11.1.106)](https://modrinth.com/mod/cloth-config/version/11.1.106+fabric) to have convenience adjusting configuration.
- [ModMenu](https://modrinth.com/mod/modmenu/versions?g=1.20.1&l=fabric) is advised as well.

## How to use?

Before you start, drop mod jar in mods folder, and launch game! (Assume you have Cloth Config API as well.)

- Open this mod's config screen (whether via ModMenu or buttons in the multiplayer screen)
- In "General settings" section, turn on "Use proxy". Choose SOCKS proxy version your proxy accepts. (SOCKS5 is highly recommended. SOCKS4 is pretty old.)
- Fill in host address port number to your proxy. Should authentication be in demand, fill in username and password. (SOCKS4 wants username field only)
- Hit save button. Done. This mod will try to reopen communication with Mojang Yggdrasil auth service, so you may pass verification upon joining servers through proxy.

## Troubleshooting

I think I have everything live, but proxy isn't working! (Can't join server. Can't accept resource pack. etc.)

- Make sure your game doesn't have other mods causing conflicts with this mod.
- Make sure your proxy is reachable live from your PC, socks version, host address, port, username and password correct.
- Make sure your dream desired Minecraft server reachable from your proxy, including
Mojang Yggdrasil auth service to verify your Minecraft character's identity. 
- Make sure your ISP isn't polluting DNS. If confirmed or unsure, navigate to "Server settings" section, turn on: 
  - "Use proxy on DNS resolve for Minecraft server when using Socks 5"
  - "Use proxy on DNS resolve for HTTP when using SOCKS5"
  - and any other option you desire.
  (By default they are on)
  (Should your proxy be polluting DNS as well, or just simply offers no DNS support, you will have to seek for other possible solution.)
- If, in the worst case, unsure about or no clue of cause (you may encounter a bug), open an issue and elaborate on what issue or problem you face using this mod.

## Upgrading

**This mod is in early development! Breaking changes will be introduced sooner or later. You are advised to backup your config file before upgrading!**

Navigate to your game's directory (usually `.minecraft`), open `config/socksproxyclient` and make copy.

## FAQs

### 1.x.x (major version) Forge/Fabric/Quilt/NeoForge please?

Maybe. I personally don't really have much time. Developing multiplatform mod (and backporting) is painful as hell,
I need to learn.

Fork for your own version, or open a PR.

### Fabric API required?

This mod doesn't declare Fabric API as a dependency, so it's a No. But you better have it.

FabricLoader 100% required.

### Fabric server applicable?

No. This mod is client-side.

### Realms applicable?

No. Realms requires vanilla client. This mod won't cover Realms part.

### Compatibility with (mod name) mod?

Depends. Should be compatible as long as (mod name) doesn't mess up with the part where Minecraft handles network connection.

As such, this mod won't be compatible with [Proxy Server](https://www.curseforge.com/minecraft/mc-mods/proxy-server)
and [RespectProxyOptions](https://modrinth.com/mod/respectproxyoptions).

Combination with [ViaFabricPlus](https://modrinth.com/mod/viafabricplus) had its multi-versioning feature failed. After manual debugging and testing, it's been resolved.
However, I am not sure whether is 100% compatible or not. Bugs may persist unnoticed, since my main focus is Java (not Bedrock).

### Will this mod ever be allowed on (server name)?

Look, you, as a simple Minecraft Java player, just want a nice game playing or have privacy protected, such desire is 100% acceptable.
You don't want to play on a server under 400+ ping through a shitty ISP network route.

Also, all code is open source, take a look in it if concerned. Besides, having a VPN yeets out this mod because your VPN software will take care of every bit of traffic for you.

Still unsure, consult server admin then.
Take Hypixel as an example, they impose inspection on your Minecraft character to see if your account is "safe".
Switching IP address frequently (or abnormally) may result in being banned because they would think your account's compromised.
Under such circumstance, better consult first.

If, unfortunately, (server name) explicitly forbids proxy (or it's pay-to-use-proxy), then issue stands at their side.

### How to see if this mod is actually working?

It will print logs when:

- Pinging a Minecraft server. (Unless you have explicitly turned off the proxy setting.)
- Joining and playing on a Minecraft server. (Unless you have explicitly turned off the proxy setting.)
- Game client makes communications with HTTP(S) servers, including:
Mojang Yggdrasil auth service, player skin download endpoint, server resource pack downloading, DoH servers.
(Unless you have explicitly turned off their proxy settings.)

So what will those logs look like?

Communications with **Minecraft servers** will look like this:
```text
[00:00:00] [Netty Client IO #1/INFO] (SocksProxyClient) Using Socks5 proxy /127.0.0.1:1080 on mc.hypixel.net/209.222.115.58
[00:00:00] [Netty Client IO #2/INFO] (SocksProxyClient) No proxy on host localhost/127.0.0.1
```

Communications with **HTTP(S) servers** will look like this:

```text
[00:00:00] [nioEventLoopGroup-6-1/INFO] (SocksProxyClient) Open tunnel to remote sessionserver.mojang.com:443
[00:00:02] [nioEventLoopGroup-6-1/INFO] (SocksProxyClient) Tunnel to sessionserver.mojang.com:443 closed.
```

In addition, when DNS resolve by this mod for Minecraft server engages, there will be logs similar to this:

```text
[00:00:00] [nioEventLoopGroup-8-1/INFO] (SocksProxyClient) Open tunnel to remote dns.google:443
[00:00:00] [Server Pinger #1/INFO] (SocksProxyClient) Resolve mc.hypixel.net to 209.222.115.23
[00:00:09] [Netty Client IO #11/INFO] (SocksProxyClient) Using Socks5 proxy /127.0.0.1:1080 on 209.222.115.23/209.222.115.23:25565
```

### Aren't proxyHost and proxyPort (and proxyUser and proxyPass) game parameters sufficient?

No. In fact, they are practically useless. Minecraft will take it as a SOCKS proxy, while JDK's HTTP client wants an HTTP proxy.
The root cause lies in JDK's native implementation.

Therefore, should you take this game-parameter approach, you likely won't pass verification upon joining servers
because your genuine IP address and proxy IP address have 0 chance of matching, unless your dream server allows
proxies.

### Initial purpose of developing this mod?

Initially to satisfy my own need. 

### Where to seek for more support?

[Open issue here.](https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/issues)


## Contributing

Bugs may persist unnoticed. Feel free to open a PR if you find and resolve a bug!

Translations are welcome. See `en_us.json` lang file in `src/main/resources/assets/socksproxyclient/lang` directory for example.
Or you may translate this README.

## License. Ending.

Licensed under [WTFPL](http://www.wtfpl.net/about/)

Leave a **star** if you find this mod useful to you!
