package org.vaadin.example.bookstore.ui.login;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Bookstore")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();

	private static final List<byte[]> leakyList = new ArrayList<>();

	public LoginView(){
		addClassName("login-view");
		setSizeFull(); 
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("login");

		add(new H1("Bookstore"));
		add(new Span("Username: user, Password: password"));
		add(new Span("Username: admin, Password: password"));
		add(login);

		if(Boolean.getBoolean("simulateMemoryLeak")) {
			// Each instance adds 10MB that is never released
			leakyList.add(new byte[1024 * 1024 * 10]);
		}
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}