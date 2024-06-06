# Changelog

## [Unreleased]

### Added

### Changed

- Compatability with [Fast IP Ping](https://modrinth.com/mod/fast-ip-ping)

### Deprecated

### Removed

### Fixed

### Security

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

[Unreleased]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.2-alpha+1.20.1...HEAD
[0.0.2-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/compare/v0.0.1-alpha+1.20.1...v0.0.2-alpha+1.20.1
[0.0.1-alpha+1.20.1]: https://github.com/CrimsonEdgeHope/SocksProxyClientOfMinecraft/commits/v0.0.1-alpha+1.20.1
