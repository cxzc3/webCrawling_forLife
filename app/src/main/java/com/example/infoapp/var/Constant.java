package com.example.infoapp.var;

public class Constant {
    public static int dCount = 8;/**날씨 화면에 표시될 뷰의 개수*/

    //WeatherFragment
    public static final String cRouteTemp         = "._hourly_weather>ul>li";
    public static final String cRouteTempTime     = "._hourly_weather>ul>li>dl>.time";
    public static final String cRouteTempWeather  = "._hourly_weather>ul>li>dl>.weather_box>i>.blind";
    public static final String cRouteTempDegree   = "._hourly_weather>ul>li>dl>.degree_point>div>div>.num";
    public static final String cRouteHumi         = "._hourly_humidity>div>div>.graph_wrap>ul>.data";
    public static final String cRoutePrec         = "._hourly_rain>div>div>.graph_wrap>ul>.data>.data_inner";
    public static final String cRoutePrecProb     = "._hourly_rain>div>div>.icon_wrap>ul>.data";

    public static final String optTemp            = "temperature";
    public static final String optTempT           = "temperature_Time";
    public static final String optTempW           = "temperature_Weather";
    public static final String optTempD           = "temperature_Degree";
    public static final String optHumi            = "humidity";
    public static final String optPrec            = "precipitation";

    public static final int statInit              = -1;
    public static final int statSet               = 0;
    public static final int statFin               = 1;
}
