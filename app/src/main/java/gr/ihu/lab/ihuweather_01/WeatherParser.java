package gr.ihu.lab.ihuweather_01;

import android.icu.text.LocaleDisplayNames;
import android.icu.text.SimpleDateFormat;
import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abdullah on 5/19/2017.
 */

public class WeatherParser {

    private static final String LOG_TAG = WeatherParser.class.getName();

    public static String[] parseWeatherFromJSON(int numDays, String jsonWeather) throws JSONException {
        String[] results = new String[numDays];

        String OWM_List="list";
        String OWM_Weather="weather";
        String OWM_Temp = "temp";
        String OWM_Max="max";
        String OWM_Min="min";
        String OWM_description="main";
        String OWM_humidity="humidity";

        //creat a josn object

        JSONObject forecastObj = new JSONObject(jsonWeather); // convert json file to java file

        JSONArray weatherArry=forecastObj.getJSONArray(OWM_List);

        //now to change the time and date


        Time dayTime= new Time();
        dayTime.setToNow();
        int julianStartDay= Time.getJulianDay(System.currentTimeMillis(),dayTime.gmtoff);
        dayTime = new Time();

        for(int i= 0;i<numDays;i++)
        {
            // we are going to use the format : DA, description , min/ max
            String day,discription,minmax;
            JSONObject dayForcast = weatherArry.getJSONObject(i);
            long dateTime;
            dateTime= dayTime.setJulianDay(julianStartDay+i);
            SimpleDateFormat sdf=  new SimpleDateFormat("EEE,dd,MMM");
          day=  sdf.format(dateTime);
            Log.i("LOG_TAG",day);

            JSONObject o = dayForcast.getJSONArray(OWM_Weather).getJSONObject(0); // this is an array but we have only one object
            discription = o.getString(OWM_description);
            JSONObject tempObj = dayForcast.getJSONObject(OWM_Temp);
            double max = tempObj.getDouble(OWM_Max);
            double min = tempObj.getDouble(OWM_Min);

            int humidity=dayForcast.getInt(OWM_humidity);
            minmax = Math.round(min)+" - " + Math.round(max); // to format the tempreture to the

            results[i]=day+" | "+ discription+ " | "+ minmax + " | " + humidity;
        }
        for(int i=0 ; i<numDays ; i++)
        {
            Log.i("log","Forcast Entry"+ results[i]);
        }
        return results;
    }
}
