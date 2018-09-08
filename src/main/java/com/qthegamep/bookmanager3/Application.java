package com.qthegamep.bookmanager3;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is main class that start application.
 */
@Slf4j
@UtilityClass
public class Application {

    public static void main(String[] args) {
        log.info(System.lineSeparator() +
                "------------------------------------------------------------------------------------------------" +
                "Book Manager 3" +
                "------------------------------------------------------------------------------------------------"
        );

        log.info("Preparing the application to start");

        System.out.println("Realization is empty!");

        log.info("Preparing the application to start was done successful");
    }
}
