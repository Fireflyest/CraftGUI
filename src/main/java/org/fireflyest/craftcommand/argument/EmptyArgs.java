package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

public class EmptyArgs implements Argument {

    @Override
    public List<String> tab(String arg) {
        return new ArrayList<>();
    }
    
}
