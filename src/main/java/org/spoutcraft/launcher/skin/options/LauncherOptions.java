/*
 * This file is part of Technic Launcher.
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
package org.spoutcraft.launcher.skin.options;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import javax.swing.*;
import net.technicpack.launchercore.exception.RestfulAPIException;
import net.technicpack.launchercore.util.*;
import org.spoutcraft.launcher.Memory;
import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.skin.LauncherFrame;
import org.spoutcraft.launcher.skin.components.BlueButton;
import org.spoutcraft.launcher.skin.components.DarkTextBox;
import org.spoutcraft.launcher.skin.components.ImageButton;
import org.spoutcraft.launcher.skin.components.LiteButton;
import org.spoutcraft.launcher.skin.components.LiteTextBox;
import org.spoutcraft.launcher.updater.LauncherInfo;
import org.spoutcraft.launcher.util.DesktopUtils;

public class LauncherOptions extends JDialog implements ActionListener, MouseListener, MouseMotionListener {
    public static final Color CHARCOAL = new Color(45, 45, 45);
    
    private static final long serialVersionUID = 1L;
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;
    private static final String LAUNCHER_PREPEND = "Launcher Build:    ";
    private static final String QUIT_ACTION = "quit";
    private static final String SAVE_ACTION = "save";
    private static final String LOGS_ACTION = "logs";
    private static final String CONSOLE_ACTION = "console";
    private static final String CHANGEFOLDER_ACTION = "changefolder";
    private static final String BETA_ACTION = "beta";
    private static final String STABLE_ACTION = "stable";
    private static final String ESCAPE_ACTION = "escape";
    private JLabel background;
    private JLabel build;
    private LiteButton logs;
    private JComboBox memory;
    private JComboBox onLaunch;
    private JRadioButton beta;
    private JRadioButton stable;
    private JFileChooser fileChooser;
    private LiteButton console;
    private int mouseX = 0, mouseY = 0;
    private String installedDirectory;
    private DarkTextBox packLocation;
    private boolean directoryChanged = false;
    private boolean streamChanged = false;
    private boolean consoleToggle = false;
    private String buildStream = "stable";

    public LauncherOptions() {
        setTitle("Launcher Options");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        addMouseListener(this);
        addMouseMotionListener(this);
        setResizable(false);
        setUndecorated(true);
        initComponents();
    }

    private void initComponents() {
        Font minecraft = LauncherFrame.getFont(12);

        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };

        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, ESCAPE_ACTION);
        getRootPane().getActionMap().put(ESCAPE_ACTION, escapeAction);

        background = new JLabel();
        background.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        LauncherFrame.setIcon(background, "optionsBackground.png", background.getWidth(), background.getHeight());

        ImageButton optionsQuit = new ImageButton(ResourceUtils.getIcon("quit.png", 28, 28), ResourceUtils.getIcon("quit.png", 28, 28));
        optionsQuit.setRolloverIcon(ResourceUtils.getIcon("quitHover.png", 28, 28));
        optionsQuit.setBounds(FRAME_WIDTH - 38, 10, 28, 28);
        optionsQuit.setActionCommand(QUIT_ACTION);
        optionsQuit.addActionListener(this);

        JLabel title = new JLabel("Launcher Options");
        title.setFont(minecraft.deriveFont(14F));
        title.setBounds(50, 10, 200, 20);
        title.setForeground(CHARCOAL);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        build = new JLabel(LAUNCHER_PREPEND + SpoutcraftLauncher.getLauncherBuild());
        build.setBounds(15, title.getY() + title.getHeight() + 10, FRAME_WIDTH - 20, 20);
        build.setFont(minecraft);
        build.setForeground(CHARCOAL);

        ButtonGroup group = new ButtonGroup();

        stable = new JRadioButton("Always use Stable Launcher Builds");
        stable.setBounds(10, build.getY() + build.getHeight() + 10, FRAME_WIDTH - 20, 20);
        stable.setFont(minecraft);
        stable.setForeground(CHARCOAL);
        stable.setContentAreaFilled(false);
        stable.setFocusPainted(false);
        stable.setBorderPainted(false);
        stable.setSelected(true);
        stable.setActionCommand(STABLE_ACTION);
        stable.addActionListener(this);
        group.add(stable);

        beta = new JRadioButton("Always use Beta Launcher Builds");
        beta.setBounds(10, stable.getY() + stable.getHeight() + 10, FRAME_WIDTH - 20, 20);
        beta.setFont(minecraft);
        beta.setForeground(CHARCOAL);
        beta.setContentAreaFilled(false);
        beta.setFocusPainted(false);
        beta.setBorderPainted(false);
        beta.setActionCommand(BETA_ACTION);
        beta.addActionListener(this);
        group.add(beta);

        buildStream = Settings.getBuildStream();
        if (buildStream.equals(Settings.STABLE)) {
            stable.setSelected(true);
        } else if (buildStream.equals(Settings.BETA)) {
            beta.setSelected(true);
        }

        JLabel memoryLabel = new JLabel("Memory: ");
        memoryLabel.setFont(minecraft);
        memoryLabel.setBounds(10, beta.getY() + beta.getHeight() + 10, 65, 20);
        memoryLabel.setForeground(CHARCOAL);
        memoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        memory = new JComboBox();
        memory.setBounds(memoryLabel.getX() + memoryLabel.getWidth() + 10, memoryLabel.getY(), 100, 20);
        populateMemory(memory);

        JLabel onLaunchLabel = new JLabel("On Pack Launch: ");
        onLaunchLabel.setFont(minecraft);
        onLaunchLabel.setBounds(10, memoryLabel.getY() + memoryLabel.getHeight() + 10, 125, 20);
        onLaunchLabel.setForeground(CHARCOAL);
        onLaunchLabel.setHorizontalAlignment(SwingConstants.CENTER);

        onLaunch = new JComboBox();
        onLaunch.setBounds(onLaunchLabel.getX() + onLaunchLabel.getWidth() + 10, onLaunchLabel.getY(), 145, 20);
        populateOnLaunch(onLaunch);

        installedDirectory = Settings.getDirectory();

        packLocation = new DarkTextBox(this, "");
        packLocation.setBounds(10, onLaunchLabel.getY() + onLaunchLabel.getHeight() + 10, FRAME_WIDTH - 20, 25);
        packLocation.setFont(minecraft.deriveFont(10F));
        packLocation.setText(installedDirectory);
        packLocation.setEnabled(false);

        LiteButton changeFolder = new BlueButton("Change Folder");
        changeFolder.setBounds(FRAME_WIDTH / 2 + 5, packLocation.getY() + packLocation.getHeight() + 10, FRAME_WIDTH / 2 - 15, 25);
        changeFolder.setFont(minecraft);
        changeFolder.setActionCommand(CHANGEFOLDER_ACTION);
        changeFolder.addActionListener(this);
        changeFolder.setEnabled(!SpoutcraftLauncher.params.isPortable());

        logs = new BlueButton("Logs");
        logs.setFont(minecraft.deriveFont(14F));
        logs.setBounds(10, packLocation.getY() + packLocation.getHeight() + 10, FRAME_WIDTH / 2 - 15, 25);
        logs.setActionCommand(LOGS_ACTION);
        logs.addActionListener(this);

        LiteButton save = new BlueButton("Save");
        save.setFont(minecraft.deriveFont(14F));
        save.setBounds(FRAME_WIDTH / 2 + 5, logs.getY() + logs.getHeight() + 10, FRAME_WIDTH / 2 - 15, 25);
        save.setActionCommand(SAVE_ACTION);
        save.addActionListener(this);

        consoleToggle = Settings.getShowConsole();
        console = new BlueButton(consoleToggle ? "Hide Console" : "Show Console");
        console.setFont(minecraft.deriveFont(14F));
        console.setBounds(10, logs.getY() + logs.getHeight() + 10, FRAME_WIDTH / 2 - 15, 25);        
        console.setActionCommand(CONSOLE_ACTION);
        console.addActionListener(this);

        fileChooser = new JFileChooser(Utils.getLauncherDirectory());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        Container contentPane = getContentPane();
        contentPane.add(build);
        contentPane.add(beta);
        contentPane.add(stable);
        contentPane.add(changeFolder);
        contentPane.add(packLocation);
        contentPane.add(logs);
        contentPane.add(console);
        contentPane.add(optionsQuit);
        contentPane.add(title);
        contentPane.add(memory);
        contentPane.add(memoryLabel);
        contentPane.add(onLaunch);
        contentPane.add(onLaunchLabel);
        contentPane.add(save);
        contentPane.add(background);

        setLocationRelativeTo(this.getOwner());
    }

    @SuppressWarnings("restriction")
    private void populateMemory(JComboBox memory) {
        long maxMemory = 1024;
        String architecture = System.getProperty("sun.arch.data.model", "32");
        boolean bit64 = architecture.equals("64");

        try {
            OperatingSystemMXBean osInfo = ManagementFactory.getOperatingSystemMXBean();
            if (osInfo instanceof com.sun.management.OperatingSystemMXBean) {
                maxMemory = ((com.sun.management.OperatingSystemMXBean) osInfo).getTotalPhysicalMemorySize() / 1024 / 1024;
            }
        } catch (Throwable t) {
        }
        maxMemory = Math.max(512, maxMemory);

        if (maxMemory >= Memory.MAX_32_BIT_MEMORY && !bit64) {
            memory.setToolTipText("<html>Sets the amount of memory assigned to Minecraft<br/>" + "You have more than 1.5GB of memory available, but<br/>"
                    + "you must have 64bit java installed to use it.</html>");
        } else {
            memory.setToolTipText("<html>Sets the amount of memory assigned to Minecraft<br/>" + "More memory is not always better.<br/>"
                    + "More memory will also cause your CPU to work more.</html>");
        }

        if (!bit64) {
            maxMemory = Math.min(Memory.MAX_32_BIT_MEMORY, maxMemory);
        }
        System.out.println("Maximum usable memory detected: " + maxMemory + " mb");

        for (Memory mem : Memory.memoryOptions) {
            if (maxMemory >= mem.getMemoryMB()) {
                memory.addItem(mem.getDescription());
            }
        }

        int memoryOption = Settings.getMemory();
        try {
            Settings.setMemory(memoryOption);
            memory.setSelectedIndex(Memory.getMemoryIndexFromId(memoryOption));
        } catch (IllegalArgumentException e) {
            memory.removeAllItems();
            memory.addItem(String.valueOf(Memory.memoryOptions[0]));
            Settings.setMemory(1); // 512 == 1
            memory.setSelectedIndex(0); // 1st element
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComponent) {
            action(e.getActionCommand(), (JComponent) e.getSource());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void action(String action, JComponent c) {
        if (action.equals(QUIT_ACTION)) {
            dispose();
        } else if (action.equals(SAVE_ACTION)) {
            int mem = Memory.memoryOptions[memory.getSelectedIndex()].getSettingsId();
            Settings.setMemory(mem);
            Settings.setBuildStream(buildStream);
            Settings.setLaunchAction((LaunchAction) onLaunch.getSelectedItem());
            if (directoryChanged) {
                Settings.setMigrate(true);
                Settings.setMigrateDir(installedDirectory);
            }

            if (directoryChanged || streamChanged) {
                JOptionPane.showMessageDialog(c, "A manual restart is required for changes to take effect. Please exit and restart your launcher.", "Restart Required", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
            dispose();
        } else if (action.equals(LOGS_ACTION)) {
            File logDirectory = new File(Utils.getLauncherDirectory(), "logs");
            DesktopUtils.open(logDirectory);
        } else if (action.equals(CONSOLE_ACTION)) {
            consoleToggle = !consoleToggle;
            Settings.setShowConsole(consoleToggle);
            if (consoleToggle) {
                SpoutcraftLauncher.setupConsole();
            } else {
                SpoutcraftLauncher.destroyConsole();
            }
            console.setText(consoleToggle ? "Hide Console" : "Show Console");
        } else if (action.equals(CHANGEFOLDER_ACTION)) {
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!ZipUtils.checkLaunchDirectory(file)) {
                    JOptionPane.showMessageDialog(c, "Please select an empty directory, or your default install folder with settings.json in it.", "Invalid Location", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                packLocation.setText(file.getPath());
                installedDirectory = file.getAbsolutePath();
                directoryChanged = true;
            }
        } else if (action.equals(BETA_ACTION)) {
            buildStream = Settings.BETA;
            build.setText(LAUNCHER_PREPEND + getLatestLauncherBuild(buildStream));
            streamChanged = true;
        } else if (action.equals(STABLE_ACTION)) {
            buildStream = Settings.STABLE;
            build.setText(LAUNCHER_PREPEND + getLatestLauncherBuild(buildStream));
            streamChanged = true;
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private int getLatestLauncherBuild(String buildStream) {
        int build = 0;
        try {
            build = Integer.parseInt(SpoutcraftLauncher.getLauncherBuild());
        } catch (NumberFormatException ignore) {
        }

        try {
            build = LauncherInfo.getLatestBuild(buildStream);
            return build;
        } catch (RestfulAPIException e) {
            e.printStackTrace();
        }

        return build;
    }

    private void populateOnLaunch(JComboBox onLaunch) {
        onLaunch.addItem(LaunchAction.HIDE);
        onLaunch.addItem(LaunchAction.CLOSE);
        onLaunch.addItem(LaunchAction.NOTHING);
        LaunchAction selectedAction = Settings.getLaunchAction();
        if (selectedAction == null) {
            onLaunch.setSelectedItem(LaunchAction.HIDE);
            Settings.setLaunchAction(LaunchAction.HIDE);
        } else {
            onLaunch.setSelectedItem(Settings.getLaunchAction());
        }
    }

}
