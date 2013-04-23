/*
 * This file is part of Love Launcher.
 *
 * Copyright (c) 2013-2013, TuttoCrafting <http://www.youtube.com/user/TuttoCrafting/>
 * Love Launcher is licensed under the Spout License Version 1.
 *
 * Love Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Love Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
/*
 * This file is part of Technic Launcher.
 *
 * Copyright (c) 2013-2013, Technic <http://www.technicpack.net/>
 * Technic Launcher is licensed under the Spout License Version 1.
 *
 * Technic Launcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Technic Launcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */

package org.spoutcraft.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import org.spoutcraft.launcher.entrypoint.SpoutcraftLauncher;
import org.spoutcraft.launcher.util.OperatingSystem;

public final class StartupParameters {
	@SuppressWarnings("unused")
	private final String[] args;
	public StartupParameters(String[] args) {
		this.args = args;
	}
	@Parameter
	private List<String> parameters = Lists.newArrayList();

	@Parameter(names = {"-username", "-user", "-u"}, description = "Minecraft Username")
	private String user = null;

	@Parameter(names = {"-password", "-pass", "-p"}, description = "Minecraft Password")
	private String pass = null;

	@Parameter(names = {"-server", "-host", "-join", "-j", "-h", "-s"}, description = "Minecraft Server to join")
	private String server = null;

	@Parameter(names = {"-portable", "--portable", "-pmode", "-portable_mode", "-pm"}, description = "Portable Mode")
	private boolean portable = false;

	@Parameter(names = {"-debug", "--debug", "-verbose", "-v", "-d"}, description = "Debug mode")
	private boolean debug = false;

	@Parameter(names = {"-proxy_host"}, description = "HTTP Proxy Host")
	private String proxyHost = null;

	@Parameter(names = {"-proxy_port"}, description = "HTTP Proxy Port")
	private String proxyPort = null;

	@Parameter(names = {"-proxy_user"}, description = "HTTP Proxy Username")
	private String proxyUser = null;

	@Parameter(names = {"-proxy_password"}, description = "HTTP Proxy Password")
	private String proxyPassword = null;

	@Parameter(names = {"-nomd5", "-ignoremd5"}, description = "Disables the MD5 checking of required files")
	private boolean ignoreMD5 = false;

	@Parameter(names = {"-build"}, description = "Uses a specific mod pack build")
	private int build = -1;

	@Parameter(names = {"-relaunched"}, description = "Used to indicate the process has been relaunched for the property memory arguments")
	private boolean relaunched = false;

	@Parameter(names = {"-old_launcher"}, description = "Indicates old launcher")
	private boolean oldLauncher = false;

	@Parameter(names = {"-console"}, description = "Shows the console window")
	private boolean console = false;

	@Parameter(names = {"-width"}, description = "Sets the width of the minecraft window to be fixed to this.")
	private int width = -1;

	@Parameter(names = {"-height"}, description = "Sets the height of the minecraft window to be fixed to this.")
	private int height = -1;

	public List<String> getParameters() {
		return parameters;
	}

	public void logParameters(Logger log) {
		log.info("------------ Startup Parameters ------------");
		if (user != null) {
			log.info("Minecraft Username: " + user);
		}
		if (pass != null) {
			log.info("Minecraft Password exists");
		}
		if (server != null) {
			log.info("Minecraft Server: " + server);
		}
		if (portable) {
			log.info("Portable mode activated");
		}
		if (debug) {
			log.info("Debug mode activated");
		}
		if (proxyHost != null) {
			log.info("Proxy Host: " + proxyHost);
		}
		if (proxyPort != null) {
			log.info("Proxy Port: " + proxyPort);
		}
		if (proxyUser != null) {
			log.info("Proxy User exists");
		}
		if (proxyPassword != null) {
			log.info("Proxy Password exists");
		}
		if (ignoreMD5) {
			log.info("No MD5 Mode activated");
		}
		if (build != -1) {
			log.info("Mod pack build selected: " + build);
		}
		if (relaunched) {
			log.info("Relaunched with correct memory");
		}
		if (oldLauncher) {
			log.info("Old Launcher detected");
		}
		if (console) {
			log.info("Console frame enabled");
		}
		if (width != -1) {
			log.info("Minecraft frame width: " + width);
		}
		if (height != -1) {
			log.info("Minecraft frame height: " + height);
		}
		log.info("--------- End of Startup Parameters ---------");
	}

	public boolean hasAccount() {
		return user != null && user.length() > 0 && pass != null && pass.length() > 0;
	}

	private boolean shouldRelaunch() {
		if (relaunched) {
			return false;
		}
		int mb = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
		int min = Memory.getMemoryFromId(Settings.getMemory()).getMemoryMB();
		boolean memory = mb < min;
		boolean permgen = Settings.getPermGen();
		return memory || permgen;
	}

	public boolean relaunch(Logger log, boolean force) {
		if (shouldRelaunch() || force) {
			String pathToJar;
			File jar = new File(SpoutcraftLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile());
			try {
				pathToJar = jar.getCanonicalPath();
			} catch (IOException e1) {
				pathToJar = jar.getAbsolutePath();
			}
			try {
				pathToJar = URLDecoder.decode(pathToJar, "UTF-8");
			} catch (java.io.UnsupportedEncodingException ignore) { }

			final int memory = Memory.getMemoryFromId(Settings.getMemory()).getMemoryMB();
			log.info("Attempting relaunch with " + memory + " mb of RAM");
			log.info("Path to Launcher Jar: " + pathToJar);

			ProcessBuilder processBuilder = new ProcessBuilder();
			ArrayList<String> commands = new ArrayList<String>();
			if (OperatingSystem.getOS().isWindows()) {
				commands.add("javaw");
			} else if (OperatingSystem.getOS().isMac()) {
				commands.add("java");
				commands.add("-Xdock:name=Spoutcraft");
			} else {
				commands.add("java");
			}
			commands.add("-Xmx" + memory + "m");
			if (Settings.getPermGen()) {
				int permSize = 128;
				if (memory >= 2048) {
					permSize = 256;
				}
				log.info("Attempting relaunch with " + "-XX:MaxPermSize=" + permSize + "m" + " as permgen");
				commands.add("-XX:MaxPermSize=" + permSize + "m");
			}
			commands.add("-cp");
			commands.add(pathToJar);
			commands.add(SpoutcraftLauncher.class.getName());
			commands.addAll(getRelaunchParameters());
			commands.add("-relaunched");
			processBuilder.command(commands);

			try {
				processBuilder.start();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private List<String> getRelaunchParameters() {
		List<String> params = new ArrayList<String>();
		if (user != null) {
			params.add("-username");
			params.add(user);
		}
		if (pass != null) {
			params.add("-password");
			params.add(pass);
		}
		if (server != null) {
			params.add("-server");
			params.add(server);
		}
		if (portable) {
			params.add("-portable");
		}
		if (debug) {
			params.add("-debug");
		}
		if (proxyHost != null) {
			params.add("-proxy_host");
			params.add(proxyHost);
		}
		if (proxyPort != null) {
			params.add("-proxy_port");
			params.add(proxyPort);
		}
		if (proxyUser != null) {
			params.add("-proxy_user");
			params.add(proxyUser);
		}
		if (proxyPassword != null) {
			params.add("-proxy_password");
			params.add(proxyPassword);
		}
		if (ignoreMD5) {
			params.add("-nomd5");
		}
		if (build != -1) {
			params.add("-build");
			params.add(Integer.toString(build));
		}
		if (console) {
			params.add("-console");
		}
		if (width != -1) {
			params.add("-width");
			params.add(Integer.toString(width));
		}
		if (height != -1) {
			params.add("-height");
			params.add(Integer.toString(height));
		}
		return params;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getServer() {
		if (server == null) {
			return null;
		}
		if (server.contains(":")) {
			return server.substring(0, server.indexOf(":"));
		}
		return server;
	}

	public String getPort() {
		if (server == null) {
			return null;
		}
		if (server.contains(":")) {
			return server.substring(server.indexOf(":") + 1);
		}
		return null;
	}

	public boolean isPortable() {
		return portable;
	}

	public boolean isDebugMode() {
		return debug;
	}

	public boolean isIgnoreMD5() {
		return ignoreMD5;
	}

	public int getSpoutcraftBuild() {
		return build;
	}

	public boolean isConsole() {
		return console;
	}

	public void setSpoutcraftBuild(int build) {
		this.build = build;
	}

	public void setupProxy() {
		Proxy proxy = new Proxy();
		proxy.setHost(this.proxyHost);
		proxy.setPort(this.proxyPort);
		proxy.setUser(this.proxyUser);
		proxy.setPass(proxyPassword != null ? this.proxyPassword.toCharArray() : null);
		proxy.setup();
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public String getProxyUser() {
		return proxyUser;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
