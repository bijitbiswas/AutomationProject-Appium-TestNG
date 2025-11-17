package mobileAutomation.utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import mobileAutomation.utilities.automationFunctions.GeneralFunction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class ServerManager extends GeneralFunction {

    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();


    public static void startServer() {
        if (service.get() == null) {
            println("Starting Appium Server");
            AppiumDriverLocalService localService = getAppiumDriverService();
            localService.start();
            if (!localService.isRunning()) {
                throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
            }

            service.set(localService);
            println("Appium Server started on: " + localService.getUrl());
        }
    }

    public static AppiumDriverLocalService getServer() {
        return service.get();
    }

    public static void stopServer() {
        if (service.get() != null) {
            println("Stopping Appium Server");
            getServer().stop();
            println("Appium Server stopped");
            service.remove();
        }
    }

    private static AppiumDriverLocalService getAppiumDriverService() {
        // Run command "appium plugin install images" to install images plugin
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.USE_PLUGINS, "images")
                .withTimeout(Duration.ofSeconds(10))
                .withLogFile(new File(createAppiumServerDirectory() + File.separator + "AppiumServer.log")));
    }

    private static String createAppiumServerDirectory() {
        Path path = Paths.get("logs");
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            println("Failed to create directory: " + e.getMessage());
        }
        return "logs";
    }

}
