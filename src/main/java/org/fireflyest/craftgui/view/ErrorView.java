package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/16 17:24
 */

public class ErrorView implements View<ErrorPage> {

    private final ErrorPage errorPage;

    public ErrorView() {
        errorPage = new ErrorPage();
    }

    @Override
    public ErrorPage getFirstPage(String target) {
        return errorPage;
    }

    @Override
    public void removePage(String target) {
    }

}
