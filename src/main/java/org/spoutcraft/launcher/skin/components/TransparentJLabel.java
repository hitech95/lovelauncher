/*
 * This file is part of Love Launcher.
 *
 * Copyright (c) 2013-2013, LovePlatform <http://loveplatform.kytech.it/>
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

package org.spoutcraft.launcher.skin.components;

import java.awt.Graphics;
import javax.swing.JLabel;

public class TransparentJLabel extends JLabel implements Transparent{
	private static final long serialVersionUID = 1L;
	TransparentComponent transparency = new TransparentComponent(this);
	public TransparentJLabel() {
		super();
	}

	public TransparentJLabel(String text) {
		super(text);
	}

	@Override
	public float getTransparency() {
		return transparency.getTransparency();
	}

	@Override
	public void setTransparency(float t) {
		transparency.setTransparency(t);
	}

	@Override
	public float getHoverTransparency() {
		return transparency.getHoverTransparency();
	}

	@Override
	public void setHoverTransparency(float t) {
		transparency.setHoverTransparency(t);
	}

	@Override
	public void paint(Graphics g) {
		g = transparency.setup(g);
		super.paint(g);
		transparency.cleanup(g);
	}
}
