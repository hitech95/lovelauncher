/*
 * ftb.paradise-reloaded.org:25575
 * Copyright (C) 2013 Syndicate, LLC
 *
 * Technic Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Technic Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Technic Launcher.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.spoutcraft.launcher.entrypoint;

import com.beust.jcommander.JCommander;
import net.technicpack.launchercore.util.Directories;
import net.technicpack.launchercore.util.OperatingSystem;
import net.technicpack.launchercore.util.Utils;
import org.apache.commons.io.IOUtils;
import org.spoutcraft.launcher.StartupParameters;
import org.spoutcraft.launcher.launcher.Launcher;
import org.spoutcraft.launcher.log.Console;
import org.spoutcraft.launcher.log.LoggerOutputStream;
import org.spoutcraft.launcher.log.RotatingFileHandler;
import org.spoutcraft.launcher.settings.LauncherDirectories;
import org.spoutcraft.launcher.skin.ErrorDialog;
import org.spoutcraft.launcher.skin.LauncherFrame;
import org.spoutcraft.launcher.skin.SplashScreen;
import org.spoutcraft.launcher.util.ShutdownThread;
import org.spoutcraft.launcher.util.TechnicLogFormatter;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpoutcraftLauncher {
    public static StartupParameters params;
    protected static Console console;
    private static RotatingFileHandler handler = null;
    private static Logger logger = null;
    private static ErrorDialog errorDialog = null;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        LauncherDirectories directories = new LauncherDirectories();
        Directories.instance = directories;

        // Prefer IPv4
        System.setProperty("java.net.preferIPv4Stack", "true");

        params = setupParameters(args);

        cleanup();

        SplashScreen splash = new SplashScreen(Toolkit.getDefaultToolkit().getImage(SplashScreen.class.getResource("/org/spoutcraft/launcher/resources/splash.png")));
        splash.setVisible(true);
        directories.setSplashScreen(splash);

        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        setLookAndFeel();

        console = new Console(params.isConsole());
        SpoutcraftLauncher.logger = setupLogger();
        console.setRotatingFileHandler(SpoutcraftLauncher.handler);

        int launcherBuild = parseInt(getLauncherBuild(), -1);
        logger.info("------------------------------------------");
        logger.info("Love Launcher is starting....");
        logger.info("Launcher Build: " + launcherBuild);

        params.logParameters(logger);

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(console));

        // Set up the launcher and load login frame
        Launcher launcher = new Launcher();

        splash.dispose();
        launcher.startup();

        logger.info("Launcher took: " + (System.currentTimeMillis() - start) + "ms to start");
    }

    public static String getLauncherBuild() {
        String build = "0";
        try {
            build = IOUtils.toString(SpoutcraftLauncher.class.getResource("/org/spoutcraft/launcher/resources/version").openStream(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return build;
    }

    private static StartupParameters setupParameters(String[] args) {
        StartupParameters params = new StartupParameters(args);
        try {
            new JCommander(params, args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        params.setupProxy();

        return params;
    }

    protected static Logger setupLogger() {
        final Logger logger = Utils.getLogger();
        File logDirectory = new File(Utils.getLauncherDirectory(), "logs");
        if (!logDirectory.exists()) {
            logDirectory.mkdir();
        }
        File logs = new File(logDirectory, "lovelauncher_%D.log");
        RotatingFileHandler fileHandler = new RotatingFileHandler(logs.getPath());

        fileHandler.setFormatter(new TechnicLogFormatter());

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }
        logger.addHandler(fileHandler);

        SpoutcraftLauncher.handler = fileHandler;

        if (params != null && !params.isDebugMode()) {
            logger.setUseParentHandlers(false);

            System.setOut(new PrintStream(new LoggerOutputStream(console, Level.INFO, logger), true));
            System.setErr(new PrintStream(new LoggerOutputStream(console, Level.SEVERE, logger), true));
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                logger.log(Level.SEVERE, "Unhandled Exception in " + t, e);

                if (errorDialog == null) {
                    LauncherFrame frame = null;

                    try {
                        frame = Launcher.getFrame();
                    } catch (Exception ex) {
                        //This can happen if we have a very early crash- before Launcher initializes
                    }

                    errorDialog = new ErrorDialog(frame, e);
                    errorDialog.setVisible(true);
                }
            }
        });

        return logger;
    }

    private static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static void setLookAndFeel() {
        OperatingSystem os = OperatingSystem.getOperatingSystem();
        if (os.equals(OperatingSystem.OSX)) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Love Launcher");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Failed to setup look and feel", e);
        }
    }

    private static void cleanup() {
        File temp = new File(Utils.getLauncherDirectory(), "temp.jar");
        temp.delete();
        temp = new File(Utils.getLauncherDirectory(), "temp.exe");
        temp.delete();
    }

    public static void setupConsole() {
        console.setupConsole();
    }

    public static void destroyConsole() {
        console.destroyConsole();
    }
}

