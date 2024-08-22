# Changelog

## [Unreleased]

### Added

### Changed

- Use Minecraft 1.21.1 Fabric Yarn mappings

### Deprecated

### Removed

### Fixed

### Security

## [0.3.1-alpha+1.21]

### Changed

- Port to Minecraft 1.21
- Explicitly declare dependency on [FabricLoader](https://github.com/FabricMC/fabric-loader/releases/tag/0.15.0) no lower than 0.15.0.
- Logger names.
- Internally refactored.

### Fixed

- Fixed missed out part of proxying legacy pinging requests.

## [0.3.0-alpha+1.20.6]

### Changed

- Port to Minecraft 1.20.5 - 1.20.6
- Patch in ClientConnectionMixin and AllowedAddressResolverMixin

## [0.2.1-alpha+1.20.4]

### Changed

- Port to Minecraft 1.20.3 - 1.20.4
- Minor patch.

## [0.2.0-alpha+1.20.2]

### Changed

- Port to Minecraft 1.20.2

### Removed

- The mod will do nothing should game client be in offline mode at launch.
- Removed "Recreate auth service" button, and all its corresponding functions.

## [0.0.3-alpha+1.20.1]

### Added

- Added new config option "Dismiss system hosts file"("minecraftRemoteResolveDismissSystemHosts") in "Server settings" section

### Changed

- Compatability with [Fast IP Ping](https://modrinth.com/mod/fast-ip-ping)

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

[Unreleased]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.3.1-alpha+1.21...HEAD
[0.3.1-alpha+1.21]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.3.0-alpha+1.20.6...v0.3.1-alpha+1.21
[0.3.0-alpha+1.20.6]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.2.1-alpha+1.20.4...v0.3.0-alpha+1.20.6
[0.2.1-alpha+1.20.4]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.2.0-alpha+1.20.2...v0.2.1-alpha+1.20.4
[0.2.0-alpha+1.20.2]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.3-alpha+1.20.1...v0.2.0-alpha+1.20.2
[0.0.3-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.2-alpha+1.20.1...v0.0.3-alpha+1.20.1
[0.0.2-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.1-alpha+1.20.1...v0.0.2-alpha+1.20.1
[0.0.1-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/commits/v0.0.1-alpha+1.20.1
