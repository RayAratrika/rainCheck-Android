package com.example.weather1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.androdocs.httprequest.HttpRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    String city = "Bangalore,In";
    String API = "b1ed46552dc89999f5a3e717d5316c4f";
    TextView place, date, tempmin, tempmax, tempnow, humidity, pressure, sunrise, sunset, winds, mon, tue, wed, thur, fri, sat, sun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        place = findViewById(R.id.place);
        date = findViewById(R.id.date);
        tempnow = findViewById(R.id.tempnow);
        tempmin = findViewById(R.id.tempmin);
        tempmax = findViewById(R.id.tempmax);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        winds = findViewById(R.id.winds);

        mon = findViewById(R.id.mon);

        new weatherTask().execute();
    }
    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.main).setVisibility(View.GONE);
        }
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");

                Long updatedAt = jsonObj.getLong("dt");
                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                String tempText = "Now\n"+main.getString("temp") + "°C";
                String updatedAtText = new SimpleDateFormat("EEE, dd MMMM", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String tempminText = "Min\n" + main.getString("temp_min") + "°C";
                String tempmaxText = "Max\n" + main.getString("temp_max") + "°C";
                String pressureText = "Pressure\n"+main.getString("pressure");
                String humidityText = "Humidity\n"+main.getString("humidity");

                String monText = main.getString("Monday")+"°C";

                Long sunriseText = sys.getLong("sunrise");
                Long sunsetText = sys.getLong("sunset");
                String windSpeed = "Winds\n"+wind.getString("speed");
                //String weatherDescription = weather.getString("description");

                place.setText(address);
                date.setText(updatedAtText);
                //statusTxt.setText(weatherDescription.toUpperCase());
                tempnow.setText(tempText);
                tempmin.setText(tempminText);
                tempmax.setText(tempmaxText);


                sunrise.setText("Sunrise\n"+new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunriseText * 1000)));
                sunset.setText("Sunset\n"+new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunsetText * 1000)));
                winds.setText(windSpeed);
                pressure.setText(pressureText);
                humidity.setText(humidityText);

                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.main).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
            }
        }
    }
}
