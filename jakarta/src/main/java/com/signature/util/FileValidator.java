package com.signature.util;


import jakarta.ejb.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Singleton
@Slf4j
public class FileValidator {

    public void validateFile(InputStream inputStream, int fileSize) {
        log.info("validating file ...");
        //TODO: Implement file validator logic
    }
}
