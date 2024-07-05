# Changelog

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security

## [0.1.3-alpha+1.16.5]

### Changed

- Explicitly declare dependency on [FabricLoader](https://github.com/FabricMC/fabric-loader/releases/tag/0.14.25) no lower than 0.14.25.
- Logger names.
- Internally refactored.

### Fixed

- Fixed missed out part of proxying legacy pinging requests.

## [0.1.2-alpha+1.16.5]

### Changed

- Minor patch.

### Removed

- Removed "Recreate auth service".

## [0.1.1-alpha+1.16.5]

### Added

- Added new config option "Dismiss system hosts file"("minecraftRemoteResolveDismissSystemHosts") in "Server settings" section

### Changed

- Declare incompatibility with [ViaFabric](https://modrinth.com/mod/viafabric)

## [0.1.0-alpha+1.16.5]

### Changed

- Port to Minecraft 1.16.5 (Java 17 minimum required.)
- Compatability with [Fast IP Ping](https://modrinth.com/mod/fast-ip-ping)

### Removed

- The mod will do nothing should game client be in offline mode at launch.
- Removed cosmetic buttons of this mod from the multiplayer screen.
- Removed "Show buttons in multiplayer screen" option from "General settings" section.
- Removed "Use proxy on Server blocklist supplier" option from "Server settings" section.

## [0.0.2-alpha+1.20.1]

### Added

- Let communication with Realms going through proxy as much as possible.

### Changed

- Renamed config option "Use proxy on Yggdrasil authentication service"("proxyYggdrasilAuth")
  to "Use proxy on Yggdrasil service interaction"("proxyYggdrasil") in "Server settings" section.
- Made patches to Mixins fetching or setting Proxy object properties.
- If game client is in offline mode, the mod will try to recreate auth services once.
  (reopen communication with Yggdrasil, and other essential services engaged in joining servers.)

### Fixed

- Fixed missed out parts of proxying on Yggdrasil.

## [0.0.1-alpha+1.20.1] - 2024-06-03

### Added

- SOCKS proxy support. Explicitly proxy Minecraft server traffic.
- Convert SOCKS proxy to HTTP(S) proxy. Proxy communications with Mojang Yggdrasil auth service, player skin download endpoint, and server resource pack downloading.
- Remotely resolve Minecraft server's domain name through SOCKS proxy with the help of [DNS over HTTPS (DoH)](https://www.rfc-editor.org/rfc/rfc8484)

[Unreleased]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.1.3-alpha+1.16.5...HEAD
[0.1.3-alpha+1.16.5]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.1.2-alpha+1.16.5...v0.1.3-alpha+1.16.5
[0.1.2-alpha+1.16.5]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.1.1-alpha+1.16.5...v0.1.2-alpha+1.16.5
[0.1.1-alpha+1.16.5]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.1.0-alpha+1.16.5...v0.1.1-alpha+1.16.5
[0.1.0-alpha+1.16.5]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.2-alpha+1.20.1...v0.1.0-alpha+1.16.5
[0.0.2-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.1-alpha+1.20.1...v0.0.2-alpha+1.20.1
[0.0.1-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/commits/v0.0.1-alpha+1.20.1
