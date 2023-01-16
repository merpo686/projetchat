import Graphics.Interface;
import ActivityManagers.*;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting chat application. port UDP=12340; port TCP=12341");
        Launcher();
    }
    /** Start the background components in charge of running the application */
    public static void Launcher() {
        LOGGER.debug("Launching application.");
        ActiveUserManager.getInstance(); //initalise the active user list
        //start_GraphicInterface(); //crystal clear
        new Interface();
        ThreadManager.getInstance(); //starts all reception threads, and send true (connected) to others
    }
}
