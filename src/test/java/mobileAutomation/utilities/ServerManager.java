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

public class ServerManager extends GeneralFunction {

    private static final ThreadLocal<AppiumDriverLocalService> service = new ThreadLocal<>();


    public static void startServer() {
        if (service.get() == null) {
            println("Starting Appium Server");

            ensureImagesPluginInstalled();

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
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE) // Kills old appium session if left running
                .withArgument(GeneralServerFlag.USE_PLUGINS, "images") // Enable images plugin for image element recognition
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error") // Logs server log only when error occurs
                .withLogFile(new File(createAppiumServerDirectory() + File.separator + "AppiumServer.log"));

        if (!osName.contains("win")) {
            String appiumPath = findAppiumPathInMacAndLinux();
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
        String path = runCommand("which", "appium");
        println("Appium path found: " + path);
        return path;
    }

    private static void ensureImagesPluginInstalled() {
        if (!isImagesPluginInstalled()) {
            String appiumCMD = getAppiumCommand();
            println("Images plugin not found. Installing...");
            String installOutput = runCommand(appiumCMD, "plugin", "install", "images");
            println(installOutput);
            if (installOutput.toLowerCase().contains("error")) {
                throw new RuntimeException("FAILED TO INSTALL IMAGES PLUGIN : " +
                        "PLEASE INSTALL MANUALLY USING COMMAND 'appium plugin install images'");
            }
        } else {
            println("Images plugin is already installed.");
        }
    }

    private static boolean isImagesPluginInstalled() {
        String appiumCMD = getAppiumCommand();
        String output = runCommand(appiumCMD, "plugin", "list");
        println(output);

        boolean imagesInstalled = false;
        for (String line : output.split("\n")) {
            // Remove ANSI color codes if present
            String lineText = line.trim().replaceAll("\u001B\\[[;\\d]*m", "");
            if (lineText.startsWith("- images@") && lineText.contains("[installed")) {
                imagesInstalled = true;
                break;
            }
        }
        
        return imagesInstalled;
    }

    private static String runCommand(String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        StringBuilder output = new StringBuilder();
        try {
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return output.toString().trim();
    }

    private static String getAppiumCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "appium.cmd";
        }
        return "appium";
    }
}
