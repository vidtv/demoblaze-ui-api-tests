package page.modal;

import com.microsoft.playwright.Locator;

/**
 * 'About us' modal window.
 * Opens after clicking 'About us' button in header.
 * </br>
 * Contains a video element with a video about the shop.
 */
public class AboutUsModal extends ModalWindow {

    // Modal title
    public static final String ABOUT_US_MODAL_TITLE = "About us";

    public AboutUsModal(Locator locator) {
        super(locator);
    }

    /**
     * Get a video element inside the modal window.
     *
     * @return locator for the video element
     */
    public Locator getAboutUsVideo() {
        return modalLocator.locator("video.vjs-tech");
    }
}
