package org.vaadin.example.bookstore.ui.localization;

import com.vaadin.flow.component.Direction;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageSwitcher extends HorizontalLayout implements LocaleChangeObserver {

    private static final Set<String> rtlSet;

    static {
        Set<String> lang = new HashSet<>();
        lang.add("ar"); // Arabic
        lang.add("dv"); // Divehi
        lang.add("fa"); // Persian
        lang.add("ha"); // Hausa
        lang.add("he"); // Hebrew
        lang.add("iw"); // Hebrew
        lang.add("ji"); // Yiddish
        lang.add("ps"); // Pushto
        lang.add("sd"); // Sindhi
        lang.add("ug"); // Uighur
        lang.add("ur"); // Urdu
        lang.add("yi"); // Yiddish

        rtlSet = Collections.unmodifiableSet(lang);
    }

    public LanguageSwitcher(Locale ...locales) {
        setClassName("language-switcher");
        setAlignItems(Alignment.CENTER);
        setSpacing(false);
        List<Locale> localeList = Arrays.stream(locales).collect(Collectors.toList());
        localeList.forEach(locale -> {
            Button btn = new Button(
                    locale.getVariant().length() > 0 ?
                            locale.getVariant() :
                            locale.getDisplayLanguage());
            btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            btn.addClickListener(e -> switchLanguage(locale));
            add(btn);
            if (localeList.indexOf(locale) < localeList.size() - 1) {
                add(new Paragraph(" |"));
            }
        });
    }

    protected void switchLanguage(Locale locale) {
        VaadinSession.getCurrent().setLocale(locale);
        UI.getCurrent().getPage().reload();
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        if (rtlSet.contains(event.getLocale().getLanguage())) {
            UI.getCurrent().setDirection(Direction.RIGHT_TO_LEFT);
        } else {
            UI.getCurrent().setDirection(Direction.LEFT_TO_RIGHT);
        }
    }
}
