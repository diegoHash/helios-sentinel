# HELIOS Sentinel migration boundary

This repository is the independent successor to the legacy CAC service.

## Current identity

- Application: `helios-sentinel`
- Java package: `com.helios.platform.sentinel`
- Public route prefix: `/helios/sentinel`
- Runtime secret prefix: `HELIOS_SENTINEL_`
- Telegram secret keys remain the approved `HELIOS_TELEGRAM_*` contract.

Existing MySQL schema, table, and column names are intentionally unchanged.
Their migration requires a separately reviewed database plan because all three
legacy APIs currently share the same RDS database.

The tag `pre-helios-rebrand-2026-07-13` identifies the imported stable baseline.
