package mobileAutomation.utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import mobileAutomation.utilities.automationFunctions.GeneralFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class ServerManager extends GeneralFunction {

    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();


    public static void startServer() {
        if (service.get() == null) {
            println("Starting Appium Server");
            AppiumDriverLocalService localService = AppiumDriverLocalService
                    .buildService(getAppiumDriverService());
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

    private static AppiumServiceBuilder getAppiumDriverService() {

        String osName = System.getProperty("os.name").toLowerCase();
        println("Operating System: " + osName);
        // Run command "appium plugin install images" to install images plugin
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.USE_PLUGINS, "images")
                .withTimeout(Duration.ofSeconds(10))
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .withLogFile(new File(createAppiumServerDirectory() + File.separator + "AppiumServer.log"));

        if (!osName.contains("win")) {
            String appiumPath = findAppiumPathInMacAndLinux();
            assert appiumPath != null;
            builder.withAppiumJS(new File(appiumPath));
        }

        return builder;
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

    private static String findAppiumPathInMacAndLinux() {
        try {
            Process process = Runtime.getRuntime().exec("which appium");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String path = reader.readLine();
            if (path != null && !path.isEmpty()) {
                println("Appium path found: " + path);
                return path.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
