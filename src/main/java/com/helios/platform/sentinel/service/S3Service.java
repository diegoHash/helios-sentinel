package com.helios.platform.sentinel.service;

import org.springframework.http.ResponseEntity;

public interface S3Service {

    ResponseEntity<byte[]> downloadFile();

}
