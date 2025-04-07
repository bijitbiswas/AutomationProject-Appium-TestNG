package mobileAutomation.utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class ServerManager {

    private AppiumDriverLocalService server;

    public AppiumDriverLocalService startServer(){
        System.out.println("========Starting Appium Server========");
        server = getAppiumDriverService();
        server.start();
        if(!server.isRunning()){
            throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
        }
        // Comment below line if you want to see server logs in the console
        server.clearOutPutStreams();
        System.out.println("========Appium Server Started========");
        return server;
    }

    public void stopServer(){
        System.out.println("========Stopping Appium Server========");
        if (server.isRunning()) {
            server.stop();
            System.out.println("========Appium Server Stopped========");
        } else
            System.out.println("========Appium Server not started or is already stopped========");
    }

    private AppiumDriverLocalService getAppiumDriverService() {
        // Run command "appium plugin install images" to install images plugin
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.USE_PLUGINS, "images")
                .withTimeout(Duration.ofSeconds(10))
                .withLogFile(new File(createAppiumServerDirectory() + File.separator + "AppiumServer.log")));
    }

    private String createAppiumServerDirectory() {
        Path path = Paths.get("logs");
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            System.out.println("Failed to create directory: " + e.getMessage());
        }
        return "logs";
    }

}
