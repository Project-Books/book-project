package com.karankumar.bookproject.ui;

import com.karankumar.bookproject.ui.challenge.ReadingChallengeView;
import com.karankumar.bookproject.ui.shelf.BooksInShelfView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MainView extends AppLayout {
    public MainView() {
        Tabs tabs = new Tabs();
        Tab myBooks = createTab(VaadinIcon.BOOK, BooksInShelfView.class, "My books");
        Tab readingChallenge = createTab(VaadinIcon.HOURGLASS, ReadingChallengeView.class, "Reading challenge");
        tabs.add(myBooks, readingChallenge);

        Anchor logout = new Anchor("/logout", "Log out");

        HorizontalLayout h = new HorizontalLayout();

        FlexLayout centreTabs = new FlexLayout();
        centreTabs.setSizeFull();
        centreTabs.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centreTabs.setAlignItems(FlexComponent.Alignment.CENTER);
        centreTabs.add(tabs);

        h.add(logout);
        h.expand(logout);

        addToNavbar(true, centreTabs, h);
    }

    private Tab createTab(VaadinIcon icon, Class<? extends Component> viewClass, String title) {
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
}
