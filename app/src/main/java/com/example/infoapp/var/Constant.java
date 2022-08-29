package com.example.infoapp.var;

public class Constant {
    public static int dCount = 8;/**날씨 화면에 표시될 뷰의 개수*/

    //WeatherFragment
    public static String cRouteTemp         = "._hourly_weather>ul>li";
    public static String cRouteHumi         = "._hourly_humidity>div>div>.graph_wrap>ul>.data";
    public static String cRoutePrec         = "._hourly_rain>div>div>.graph_wrap>ul>.data>.data_inner";
    public static String cRoutePrecProb     = "._hourly_rain>div>div>.icon_wrap>ul>.data";

    public static String optTemp            = "temperature";
    public static String optHumi            = "humidity";
    public static String optPrec            = "precipitation";

    public static int statSet               = 0;
    public static int statFin               = 1;
}
