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
package org.spoutcraft.launcher.technic;

import java.awt.Image;
import java.util.Collections;
import java.util.List;

import org.spoutcraft.launcher.skin.MetroLoginFrame;
import org.spoutcraft.launcher.technic.rest.pack.RestModpack;

public class AddPack extends PackInfo {
	private final static Image icon = MetroLoginFrame.getIcon("icon.png", 32, 32).getImage();
	private final static Image logo = MetroLoginFrame.getIcon("addNewPack.png", 180, 110).getImage();
	private final static Image background = MetroLoginFrame.getIcon("background.jpg", 880, 520).getImage().getScaledInstance(880, 520, Image.SCALE_SMOOTH);

	@Override
	public String getName() {
		return "addpack";
	}

	@Override
	public String getDisplayName() {
		return "Add Pack";
	}

	@Override
	public Image getBackground() {
		return background;
	}

	@Override
	public Image getLogo() {
		return logo;
	}

	@Override
	public Image getIcon() {
		return icon;
	}

	@Override
	public String getRecommended() {
		return "";
	}

	@Override
	public String getLatest() {
		return "";
	}

	@Override
	public boolean isLoading() {
		return false;
	}

	@Override
	public List<String> getBuilds() {
		return Collections.emptyList();
	}

	@Override
	public RestModpack getModpack() {
		return null;
	}
}
