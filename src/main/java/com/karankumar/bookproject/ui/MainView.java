/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.ui.goal.ReadingGoalView;
import com.karankumar.bookproject.ui.settings.SettingsView;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.karankumar.bookproject.ui.statistics.StatisticsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;

import java.util.HashMap;

@CssImport("./styles/shared-styles.css")
public class MainView extends AppLayout implements RouterLayout, PageConfigurator {
    public MainView() {
        Anchor logout = createLogoutLink();
        HorizontalLayout logoutLayout = new HorizontalLayout();
        logoutLayout.add(logout);
        logoutLayout.expand(logout);

        addToNavbar(true, centerTabs(addTabs()), logoutLayout);
    }

    private Anchor createLogoutLink() {
        Anchor logout = new Anchor("/logout", "Log out");
        logout.addClassName("logout");
        return logout;
    }

    private Tabs addTabs() {
        Tab myBooks = createTab(VaadinIcon.BOOK, BooksInShelfView.class, "My books");
        Tab readingChallenge = createTab(VaadinIcon.HOURGLASS, ReadingGoalView.class, "Goal");
        Tab stats = createTab(VaadinIcon.TRENDING_UP, StatisticsView.class, "Statistics");
        Tab settings = createTab(VaadinIcon.COG_O, SettingsView.class, "Settings");

        Tabs tabs = new Tabs();
        tabs.add(myBooks, readingChallenge, stats, settings);

        return tabs;
    }

    private static Tab createTab(VaadinIcon icon, Class<? extends Component> viewClass,
                                 String title) {
        return createTab(addLink(new RouterLink(null, viewClass), icon, title));
    }

    private static Tab createTab(Component component) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(component);
        return tab;
    }

    private static <T extends HasComponents> T addLink(T a, VaadinIcon icon, String title) {
        a.add(icon.create());
        a.add(title);
        return a;
    }

    private FlexLayout centerTabs(Tabs tabs) {
        FlexLayout centreTabs = new FlexLayout();
        centreTabs.setSizeFull();
        centreTabs.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centreTabs.setAlignItems(FlexComponent.Alignment.CENTER);
        centreTabs.add(tabs);
        return centreTabs;
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("rel", "shortcut icon");
        attributes.put("type", "image/png");
        settings.addLink("icons/icon.png", attributes);
    }
}
