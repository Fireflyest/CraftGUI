package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/16 17:24
 */

public class ErrorView implements View<ErrorPage> {

    public static final String NOT_FOUND = "NOT_FOUND";

    private final Map<String, ErrorPage> pageMap = new HashMap<>();
    private final ErrorPage errorPage;

    public ErrorView() {
        errorPage = new ErrorPage();
        pageMap.put(NOT_FOUND, errorPage);
    }

    @Override
    public ErrorPage getFirstPage(String target) {
        return pageMap.getOrDefault(target, errorPage);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }

}
