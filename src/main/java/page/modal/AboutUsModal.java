package page.modal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AboutUsModal extends ModalWindow {

    // Modal title
    public static final String ABOUT_US_MODAL_TITLE = "About us";

    public AboutUsModal(Page page) {
        super(page);
    }

    public Locator getModalTitle() {
        return page.locator("#videoModalLabel");
    }

    public Locator getHeaderCloseButton() {
        return page.locator("#videoModal").getByLabel("Close");
    }

    public Locator getAboutUsVideo() {
        return page.locator("video.vjs-tech");
    }

    public Locator getCloseButton() {
        return page.locator("#videoModal").getByText("Close", new Locator.GetByTextOptions().setExact(true));
    }
}
