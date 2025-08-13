package page.modal;

import com.microsoft.playwright.Page;

public abstract class ModalWindow {
    protected Page page;

    public ModalWindow(Page page) {
        this.page = page;
    }
}
