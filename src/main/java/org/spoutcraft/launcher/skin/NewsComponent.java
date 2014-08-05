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
package org.spoutcraft.launcher.skin;

import net.technicpack.launchercore.exception.RestfulAPIException;
import it.kytech.launchercore.restful.InternalPlatformConstants;
import net.technicpack.launchercore.restful.RestObject;
import it.kytech.launchercore.restful.platform.Article;
import it.kytech.launchercore.restful.platform.News;
import net.technicpack.launchercore.util.Utils;
import org.spoutcraft.launcher.skin.components.HyperlinkJTextPane;
import org.spoutcraft.launcher.skin.components.RoundedBox;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;

public class NewsComponent extends JComponent {

    public NewsComponent() {
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(LauncherFrame.getFont(22));
    }

    public void loadArticles() {
        try {
            List<Article> articles = RestObject.getRestObject(News.class, InternalPlatformConstants.NEWS).getNews();
            setupArticles(articles);
        } catch (RestfulAPIException e) {
            Utils.getLogger().log(Level.WARNING, "Unable to load news, hiding news section", e);
            this.setVisible(false);
            this.setEnabled(false);
        }
    }

    private void setupArticles(List<Article> articles) {
        Font articleFont = LauncherFrame.getFont(12);
        int width = getWidth() - 16;
        int height = getHeight() / 2 - 16;

        for (int i = 0; i < 2; i++) {
            Article article = articles.get(i);
            String date = article.getDate().split(" ")[0];
            String title = article.getDisplayTitle();
            HyperlinkJTextPane link = new HyperlinkJTextPane(date + "\n" + title, article.getUrl());
            link.setFont(articleFont);
            link.setForeground(Color.WHITE);
            link.setBackground(new Color(255, 255, 255, 0));
            link.setBounds(8, 8 + ((height + 8) * i), width, height);
            this.add(link);
        }

        RoundedBox background = new RoundedBox(LauncherFrame.TRANSPARENT);
        background.setBounds(0, 0, getWidth(), getHeight());
        this.add(background);
        this.repaint();
    }
}
