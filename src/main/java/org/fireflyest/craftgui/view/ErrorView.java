package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.View;

/**
 * @author Fireflyest
 * 2022/2/16 17:24
 */

public class ErrorView implements View<ErrorPage> {

    private final ErrorPage errorPage;

    public ErrorView() {
        errorPage = new ErrorPage(null);
    }

    @Override
    public ErrorPage getFirstPage(String target) {
        return errorPage;
    }

    @Override
    public void removePage(String target) {
    }

}
