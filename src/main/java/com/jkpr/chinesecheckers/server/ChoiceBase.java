package com.jkpr.chinesecheckers.server;

import java.util.ArrayList;
import java.util.HashMap;

public class ChoiceBase {
    private  HashMap<String,String[]> base=new HashMap<String, String[]>();
    private ArrayList<String> types=new ArrayList<>();
    public ChoiceBase(){
        base.put("Wybierz typ",new String[]{"wybierz liczbe"});
        types.add("Wybierz typ");

        base.put("Standard", new String[]{"wybierz liczbe","2", "3", "4", "6"});
        types.add("Standard");

        base.put("Fast Paced", new String[]{"wybierz liczbe","2", "3", "4", "6"});
        types.add("Fast Paced");

        base.put("Yin and Yang", new String[]{"2"});
        types.add("Yin and Yang");
    }
    public  String[] getKeys(){
        return types.toArray(new String[0]);
    }
    public String[] getArray(String key){
        return base.get(key);
    }
}
