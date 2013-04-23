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

package org.spoutcraft.launcher.skin.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JProgressBar;

import org.spoutcraft.launcher.skin.MetroLoginFrame;

public class LiteProgressBar extends JProgressBar implements Transparent{
	private static final long serialVersionUID = 1L;
	private final TransparentComponent transparency = new TransparentComponent(this, false);
	private final MetroLoginFrame frame;

	public LiteProgressBar(MetroLoginFrame frame) {
		setFocusable(false);
		setOpaque(false);
		this.frame = frame;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) transparency.setup(g);

		// Draw bar
		g2d.setColor(new Color(45, 45, 45));
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// Draw progress
		g2d.setColor(new Color(210, 210, 210));
		int x = (int) (getWidth() * getPercentComplete());
		g2d.fillRect(0, 0, x, getHeight());

		transparency.cleanup(g2d);
		g2d = (Graphics2D) g;

		if (this.isStringPainted() && getString().length() > 0) {
			g2d.setFont(getFont());
			
			final int startWidth = (getWidth() - g2d.getFontMetrics().stringWidth(getString())) / 2;
			String white = "";
			int whiteWidth = 0;
			int chars = 0;
			for (int i = 0; i < getString().length(); i++) {
				white += getString().charAt(i);
				whiteWidth = g2d.getFontMetrics().stringWidth(white);
				if (startWidth + whiteWidth > x) {
					break;
				}
				chars++;
			}
			if (chars != getString().length()) {
				white = white.substring(0, white.length() - 1);
				whiteWidth = g2d.getFontMetrics().stringWidth(white);
			}
			float height = (this.getHeight() / 2) + (getFont().getSize() / 2);
			g2d.setColor(new Color(45, 45, 45));
			g2d.drawString(white, startWidth, height);
			g2d.setColor(new Color(210, 210, 210));
			g2d.drawString(this.getString().substring(chars), whiteWidth + startWidth, height);
		}

		transparency.cleanup(g2d);
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		frame.getBarBox().setVisible(aFlag);
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
}
