package com.helios.platform.sentinel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks")
@CrossOrigin(origins = "*") // Para aceptar webhooks externos
public class WebhookController {

    @PostMapping("/autorizaciones")
    public ResponseEntity<String> receiveAuthorizationWebhook(@RequestBody Map<String, Object> payload) {
        // Aquí se procesarán las peticiones entrantes desde otros sistemas (ej. WhatsApp, Telegram, o Landing)
        System.out.println("🔔 Webhook recibido: " + payload.toString());

        // Logica futura: Validar firma, leer datos de autorización y actualizar BD

        return ResponseEntity.ok("Webhook de autorización recibido correctamente");
    }
}
