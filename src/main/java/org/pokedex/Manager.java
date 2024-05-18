package org.pokedex;

import java.util.ArrayList;

public interface Manager {
    public void refresh();

    public void preImg();

    public void nextImg();

    public ArrayList<Long> getCurrentStats();

    public String getCurName();

    public String getCurAbilities();

    public String getCurTypes();

    public String getCurImg();
}
