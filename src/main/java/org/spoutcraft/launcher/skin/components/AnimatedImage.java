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
package org.spoutcraft.launcher.skin.components;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class AnimatedImage extends JLabel implements ActionListener {
    private static final long serialVersionUID = 1;
    private final Timer timer;

    public AnimatedImage(Icon image, int delay) {
        this.setIcon(image);
        timer = new Timer(delay, this);
    }

    public void setAnimating(boolean animate) {
        if (animate) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public Timer getTimer() {
        return timer;
    }
}
