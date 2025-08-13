package page;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import page.modal.LoginModal;
import page.modal.SignUpModal;

import static utils.Constants.BASE_URL;

public class MainPage {
    private final Page page;

    public final LoginModal loginModal;
    public final SignUpModal signUpModal;

    public MainPage(Page page) {
        this.page = page;

        this.loginModal = new LoginModal(page);
        this.signUpModal = new SignUpModal(page);
    }

    public void navigate() {
        page.navigate(BASE_URL);
    }

    public Locator getLoginButton() {
        return page.locator("#login2");
    }

    public Locator getUsername() {
        return page.locator("#nameofuser");
    }
}
