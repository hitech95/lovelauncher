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
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LiteTextBox extends JTextField implements FocusListener{
	private static final long serialVersionUID = 1L;
	protected final JLabel label;
	public LiteTextBox(JFrame parent, String label) {
		this.label = new JLabel(label);
		addFocusListener(this);
		parent.getContentPane().add(this.label);
		this.setBackground(new Color(220, 220, 220));
		this.setBorder(new LiteBorder(5, getBackground()));
		this.label.setForeground(Color.BLACK);
		this.setForeground(Color.BLACK);
		this.setCaretColor(Color.BLACK);
	}
	
	public LiteTextBox(JDialog parent, String label) {
		this.label = new JLabel(label);
		addFocusListener(this);
		parent.getContentPane().add(this.label);
		this.setBackground(new Color(220, 220, 220));
		this.setBorder(new LiteBorder(5, getBackground()));
		this.label.setForeground(Color.BLACK);
		this.setForeground(Color.BLACK);
		this.setCaretColor(Color.BLACK);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		if (label != null) {
			label.setFont(font);
		}
	}

	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		label.setBounds(x + 5, y + 3, w - 5, h - 5);
	}

	@Override
	public void focusGained(FocusEvent e) {
		label.setVisible(false);
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (getText().length() == 0) {
			label.setVisible(true);
		}
	}

	public void setLabelVisible(boolean visible) {
		label.setVisible(visible);
	}
}
