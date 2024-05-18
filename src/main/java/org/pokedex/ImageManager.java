package org.pokedex;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ImageManager implements Manager{
    private List<String> imgFiles = new ArrayList<>();
    private int curImgIndex = 0;

    public ImageManager() {
        File folder = new File("src/images");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    imgFiles.add(file.getName());
                }
            }
        }
    }

    @Override
    public void refresh() {
        imgFiles.clear();
        File folder = new File("src/images");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    imgFiles.add(file.getName());
                }
            }
        }
    }

    @Override
    public void preImg() {
        curImgIndex--;
        if (curImgIndex < 0) {
            curImgIndex = imgFiles.size() - 1;
        }
    }

    @Override
    public void nextImg() {
        curImgIndex++;
        if (curImgIndex >= imgFiles.size()) {
            curImgIndex = 0;
        }
    }

    @Override
    public ArrayList<Long> getCurrentStats() {
        ArrayList<String> stats = new ArrayList<>();
        String curPokeName = imgFiles.get(curImgIndex).split("\\.")[0];
        ArrayList<Long> statList = null;


        try {
            Object obj = new JSONParser().parse(new FileReader("src/json/"+curPokeName+".json"));
            JSONObject pokeObj = (JSONObject) obj;
            statList = (ArrayList<Long>) pokeObj.get("stats");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return statList;
    }

    @Override
    public String getCurName() {
        return imgFiles.get(curImgIndex).split("\\.")[0].toUpperCase();
    }

    @Override
    public String getCurAbilities() {
        String curPokeName = imgFiles.get(curImgIndex).split("\\.")[0];
        String abilities = "";
        try {
            Object obj = new JSONParser().parse(new FileReader("src/json/"+curPokeName+".json"));
            JSONObject pokeObj = (JSONObject) obj;
            abilities = (String) pokeObj.get("abilities");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return abilities;
    }

    @Override
    public String getCurTypes() {
        String curPokeName = imgFiles.get(curImgIndex).split("\\.")[0];
        String type = "";
        try {
            Object obj = new JSONParser().parse(new FileReader("src/json/"+curPokeName+".json"));
            JSONObject pokeObj = (JSONObject) obj;
            type = (String) pokeObj.get("types");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    @Override
    public String getCurImg() {
        return imgFiles.get(curImgIndex);
    }
}

