# AWS Secrets Manager integration for Sentinel API

## Scope

The first production secret is:

```text
helios/prod/sentinel/telegram
```

It contains only these JSON keys:

```text
HELIOS_TELEGRAM_BOT_TOKEN
HELIOS_TELEGRAM_CHAT_ID
HELIOS_TELEGRAM_BOT_USERNAME
```

Secret values must never be copied into Git, deployment workflows, systemd unit files, logs, command arguments, or this document.

## Runtime identity

Production uses the EC2 instance role `CaribbeanOneProdEc2RuntimeRole`. The role has an attached customer-managed policy that grants only `secretsmanager:GetSecretValue` and `secretsmanager:DescribeSecret` for the exact approved secret ARN. No static AWS access keys are required.

## Configuration loading

Local development keeps the optional `.env.prod` import and does not contact AWS by default. Production enables two ordered property sources through `HELIOS_CONFIG_IMPORT`:

```text
optional:file:.env.prod[.properties]
```

The second entry is formed by concatenating the Spring prefix `optional:aws-secretsmanager:` with the secret name `helios/prod/sentinel/telegram`. At runtime, join the file entry and the resulting Secrets Manager entry with a comma and no whitespace.

The Telegram secret is optional during the migration. If it cannot be retrieved, Sentinel continues serving requests and skips Telegram notifications. Mandatory secrets such as database credentials and JWT signing keys will not use the `optional` prefix when their migrations begin.

`AWS_REGION` must be `us-east-1` for this secret. Spring Cloud AWS uses the default AWS credentials provider chain, which obtains temporary credentials from the EC2 instance profile in production.

## Migration sequence

1. Deploy the application with the Secrets Manager integration and the existing environment fallback.
2. Enable `HELIOS_CONFIG_IMPORT` using a systemd drop-in, never by editing the committed unit with values.
3. Restart and verify health without retrieving or printing the secret.
4. Confirm a Telegram synthetic notification.
5. Remove the legacy Telegram values from the EC2 `.env` file.
6. Restart and repeat health and notification checks.
7. Rotate the previously exposed bot token and update the secret version.
8. Restart, verify the new `AWSCURRENT` version, and revoke the old token.

## Rollback

During the first migration, remove the Secrets Manager entry from `HELIOS_CONFIG_IMPORT` and restart to use the existing environment fallback. After rotation, move the known-good version stage back to `AWSCURRENT` or restore the previous application configuration according to the rotation runbook. Never print `SecretString` while diagnosing a failure.
