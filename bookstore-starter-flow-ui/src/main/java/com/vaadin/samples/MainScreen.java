package com.vaadin.samples;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * Content of the UI when the user is logged in.
 * 
 * 
 */
public class MainScreen extends HorizontalLayout {
    private Menu menu;

    public MainScreen() {

        setSpacing(false);
        setStyleName("main-screen");

        CssLayout viewContainer = new CssLayout();
        viewContainer.addStyleName("valo-content");
        viewContainer.setSizeFull();

        //TODO must be converted to Flow version in next steps
//        final Navigator navigator = new Navigator(ui, viewContainer);
//        navigator.setErrorView(ErrorView.class);
//        menu = new Menu(navigator);
//        menu.addView(new SampleCrudView(), SampleCrudView.VIEW_NAME,
//                SampleCrudView.VIEW_NAME, VaadinIcons.EDIT);
//        menu.addView(new AboutView(), AboutView.VIEW_NAME, AboutView.VIEW_NAME,
//                VaadinIcons.INFO_CIRCLE);
//
//        navigator.addViewChangeListener(viewChangeListener);

        addComponent(menu);
        addComponent(viewContainer);
        setExpandRatio(viewContainer, 1);
        setSizeFull();
    }

    // notify the view menu about view changes so that it can display which view
    // is currently active
    ViewChangeListener viewChangeListener = new ViewChangeListener() {

        @Override
        public boolean beforeViewChange(ViewChangeEvent event) {
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeEvent event) {
            menu.setActiveView(event.getViewName());
        }

    };
}
