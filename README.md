# HELIOS Sentinel

API de HELIOS Platform para control de acceso, propietarios, familiares,
encargados, vehículos, perfiles y monitoreo operativo. Es el sucesor independiente
de CAC; el servicio legacy permanece intacto.

## Requisitos

- Java 21
- MySQL 8
- AWS Secrets Manager en producción
- Credenciales AWS obtenidas mediante el IAM Role de EC2, nunca mediante claves estáticas

## Ejecución local

Configura las variables indicadas en [`.env.example`](.env.example) y ejecuta:

```powershell
$env:HELIOS_SWAGGER_ENABLED="true"
.\gradlew.bat bootRun
```

La API escucha en `http://localhost:8080`. Para verificarla:

```powershell
.\gradlew.bat clean test
```

## Swagger / OpenAPI

Swagger está deshabilitado por defecto y debe habilitarse solo en desarrollo o
mediante acceso administrativo controlado:

- Swagger UI local: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON local: `http://localhost:8080/v3/api-docs`
- Swagger agregado en Gateway: `https://<gateway>/swagger-ui.html`
- OpenAPI mediante Gateway: `https://<gateway>/docs/sentinel/v3/api-docs`

Usa **Authorize** con `Bearer <token>`. El documento se genera directamente desde
los controladores de autenticación, acceso, propietarios, carga familiar,
perfiles, usuarios administrativos y webhooks.

## Contrato HTTP

- Prefijo público en Gateway: `/helios/sentinel/**`
- Gateway retira ese prefijo antes de consultar el servicio
- Identidad Spring: `helios-sentinel`
- Paquete Java: `com.helios.platform.sentinel`
- Autenticación: JWT Bearer

## Secrets Manager

El secreto de Telegram aprobado es `helios/prod/sentinel/telegram` y utiliza:

- `HELIOS_TELEGRAM_BOT_TOKEN`
- `HELIOS_TELEGRAM_CHAT_ID`
- `HELIOS_TELEGRAM_BOT_USERNAME`

Otros grupos sensibles usan el prefijo `HELIOS_SENTINEL_`. Consulta
[`docs/AWS_SECRETS_MANAGER.md`](docs/AWS_SECRETS_MANAGER.md) antes de modificar la
carga de secretos.

No ejecutes `aws configure` en EC2 ni guardes access keys, tokens o contraseñas en
el repositorio. Los nombres físicos del esquema MySQL se mantienen por
compatibilidad; consulta [`HELIOS_MIGRATION.md`](HELIOS_MIGRATION.md).

## Despliegue seguro

Swagger debe permanecer deshabilitado en producción salvo que el Gateway o una
capa de acceso administrativo lo proteja. Todo cambio debe pasar pruebas y
escaneo de secretos antes de promoverse a `main`.
