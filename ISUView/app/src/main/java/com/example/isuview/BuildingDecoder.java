package com.example.isuview;

public class BuildingDecoder {
    static final String gilman = "Gilman Hall";
    static final String atanasoff = "Atanasoff Hall";
    static final String pearson = "Pearson Hall";
    public final static int decode(String address){
        switch (address){
            case gilman:
                return R.drawable.gilman1;
            case atanasoff:
                return R.drawable.atan_1;
            case pearson:
                return R.drawable.pearson_1;
            default:
                return R.drawable.gilman1;
        }
    }

    public static String getName(String address){
        switch (address){
            case gilman:
                return "Gilman";
            case atanasoff:
                return "Atanasoff";
            case pearson:
                return "Pearson";
            default:
                return "Gilman";
        }
    }
    public static String getStartRoom(String address){
        switch (address){
            case gilman:
                return "1311";
            case atanasoff:
                return "";
            case pearson:
                return "1156";
            default:
                return "1311";
        }
    }
    public static String getEndRoom(String address){
        switch (address){
            case gilman:
                return "1656";
            case atanasoff:
                return "";
            case pearson:
                return "1124";
            default:
                return "1656";
        }
    }
}
