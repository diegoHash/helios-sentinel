package com.helios.platform.sentinel.error;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogFileTest {

    @BeforeAll
    public static void initLog(){
        LogFile.init();
    }

    @Test
    void writeLogErrors() throws Exception {

        //Avoid to reach a NullPointerException because throws
        // Null message in Telegram Notification Service
        LogFile.writeLogError(new BadCredentialsException(""), true);
        assertTrue(LogFile.errorLogFile.exists());

    }

    @Test
    void writeLogErrorCustomMssgeNull(){
        assertThrows(IllegalArgumentException.class,() ->
                LogFile.writeLogError(new BadCredentialsException(""), ""));
    }


    @Test
    void writeLogAction() {


        assertThrows(IllegalArgumentException.class,() ->
                LogFile.writeLogAction(""));
    }
}
