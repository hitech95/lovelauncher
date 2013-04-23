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

package org.spoutcraft.launcher.skin;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Vector;

public class SpoutFocusTraversalPolicy extends FocusTraversalPolicy {
	Vector<Component> order;

	public SpoutFocusTraversalPolicy(Vector<Component> order) {
		this.order = new Vector<Component>(order.size());
		this.order.addAll(order);
	}

	@Override
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
		int idx = (order.indexOf(aComponent) + 1) % order.size();
		return order.get(idx);
	}

	@Override
	public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
		int idx = order.indexOf(aComponent) - 1;
		if (idx < 0) {
			idx = order.size() - 1;
		}
		return order.get(idx);
	}

	@Override
	public Component getDefaultComponent(Container focusCycleRoot) {
		return order.get(0);
	}

	@Override
	public Component getLastComponent(Container focusCycleRoot) {
		return order.lastElement();
	}

	@Override
	public Component getFirstComponent(Container focusCycleRoot) {
		return order.get(0);
	}
}
