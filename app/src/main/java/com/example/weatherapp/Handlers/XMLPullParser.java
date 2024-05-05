package com.example.weatherapp.Handlers;

import android.util.Log;

import com.example.weatherapp.Models.Day;
import com.example.weatherapp.Models.Observation;
import com.example.weatherapp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// Ameerat Ademuyiwa - S2038600


public class XMLPullParser {

    public static List<Day> parseXML(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        xmlPullParser.setInput(inputStream, null);

        List<Day> days = new ArrayList<>();

        int eventType = xmlPullParser.getEventType();
        while (eventType!= XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = xmlPullParser.getName();
                if (tagName.equalsIgnoreCase("item")) {
                    Day day = parseItem(xmlPullParser);
                    days.add(day);
                }
            }

            eventType = xmlPullParser.next();
        }

        return days;
    }

    private static Day parseItem(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Day day = new Day();
        xmlPullParser.require(XmlPullParser.START_TAG, null, "item");

        String title = null;
        String description = null;

        while (xmlPullParser.next()!= XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType()!= XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = xmlPullParser.getName();
            if (tagName.equalsIgnoreCase("title")) {
                title = readText(xmlPullParser);
            } else if (tagName.equalsIgnoreCase("description")) {
                description = readText(xmlPullParser);
            } else {
                skipTag(xmlPullParser);
            }
        }

        if (title!= null && description!= null) {
            String[] titleParts = title.split(", ");
            if (titleParts.length >= 2) {
                String dayAndCondition = titleParts[0];
                String[] dayAndConditionParts = dayAndCondition.split(": ");
                if (dayAndConditionParts.length >= 2) {
                    day.setDay(dayAndConditionParts[0].trim());
                    day.setWeatherCondition(dayAndConditionParts[1].trim());
                }
            }

            // Extracting sunrise and sunset from the description using regular expressions
            Pattern sunrisePattern = Pattern.compile("Sunrise: (\\d{2}:\\d{2})");
            Pattern sunsetPattern = Pattern.compile("Sunset: (\\d{2}:\\d{2})");
            Matcher sunriseMatcher = sunrisePattern.matcher(description);
            Matcher sunsetMatcher = sunsetPattern.matcher(description);

            if (sunriseMatcher.find()) {
                String sunriseTime = sunriseMatcher.group(1);
                day.setSunrise(sunriseTime);
            }
            if (sunsetMatcher.find()) {
                String sunsetTime = sunsetMatcher.group(1);
                day.setSunset(sunsetTime);
            }

            // Extracting other weather data from the description
            String[] descriptionParts = description.split(", ");
            for (String part : descriptionParts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    switch (key) {
                        case "Minimum Temperature":
                            day.setMinimumTemperature(value);
                            break;
                        case "Maximum Temperature":
                            day.setMaximumTemperature(value);
                            break;
                        case "Wind Direction":
                            day.setWindDirection(value);
                            break;
                        case "Wind Speed":
                            day.setWindSpeed(value);
                            break;
                        case "Visibility":
                            day.setVisibility(value);
                            break;
                        case "Pressure":
                            day.setPressure(value);
                            break;
                        case "Humidity":
                            day.setHumidity(value);
                            break;
                        case "UV Risk":
                            day.setUvRisk(value);
                            break;
                        case "Pollution":
                            day.setPollution(value);
                            break;
                        default:
                            break;
                    }
                    Log.d("XMLPullParser", "Key: " + key + ", Value: " + value);
                }
            }

            // Map weather condition to image resource
            int imageResource = mapWeatherConditionToImageResource(day.getWeatherCondition());
            day.setImageResource(imageResource);
        }

        return day;
    }

    private static String readText(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        String result = "";
        if (xmlPullParser.next() == XmlPullParser.TEXT) {
            result = xmlPullParser.getText();
            xmlPullParser.nextTag();
        }
        return result;
    }

    private static void skipTag(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType()!= XmlPullParser.START_TAG) {
            throw new IllegalStateException("Expected start tag");
        }
        int depth = 1;
        while (depth!= 0) {
            switch (xmlPullParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // Method to map weather conditions to image resources
    private static int mapWeatherConditionToImageResource(String weatherCondition) {
        switch (weatherCondition.toLowerCase()) {
            case "sunny":
                return R.drawable.sunny;
            case "cloudy":
                return R.drawable.cloudy;
            case "hazy":
                return R.drawable.hazy;
            case "light rain showers":
                return R.drawable.light_rain_showers;
            case "light rain":
                return R.drawable.light_rain;
            case "clear sky":
                return R.drawable.clear_sky;
            case "sunny intervals":
                return R.drawable.sunny_intervals;
            case "light cloud":
                return R.drawable.light_cloud;
            case "partly cloudy":
                return R.drawable.partly_cloud;
            default:
                return R.drawable.overcast; // Default image if no match found
        }
    }

    public static Observation parseObservation(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        xmlPullParserFactory.setNamespaceAware(true);
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        xmlPullParser.setInput(inputStream, null);

        Observation observation = null;

        int eventType = xmlPullParser.getEventType();
        while (eventType!= XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String tagName = xmlPullParser.getName();
                if (tagName.equalsIgnoreCase("item")) {
                    observation = parseObservationItem(xmlPullParser);
                    // Log the extracted observation data
                    Log.d("XMLParser", "Extracted Observation: " + observation.toString());
                }
            }

            eventType = xmlPullParser.next();
        }

        return observation;
    }

    private static Observation parseObservationItem(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Observation observation = new Observation();
        xmlPullParser.require(XmlPullParser.START_TAG, null, "item");

        String title = null;
        String description = null;
        String pubDate = null;

        while (xmlPullParser.next()!= XmlPullParser.END_TAG) {
            if (xmlPullParser.getEventType()!= XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = xmlPullParser.getName();
            if (tagName.equalsIgnoreCase("title")) {
                title = readText(xmlPullParser);
            } else if (tagName.equalsIgnoreCase("description")) {
                description = readText(xmlPullParser);
            } else if (tagName.equalsIgnoreCase("pubDate")) {
                pubDate = readText(xmlPullParser);
            } else {
                skipTag(xmlPullParser);
            }
        }

        if (title!= null && description!= null && pubDate!= null) {
            // Splitting the title to extract the day and weather condition
            String[] titleParts = title.split(", ");
            if (titleParts.length >= 2) {
                String dayAndCondition = titleParts[0];
                String[] dayAndConditionParts = dayAndCondition.split(": ");
                if (dayAndConditionParts.length >= 2) {
                    observation.setDay(dayAndConditionParts[0].trim());
                    observation.setWeatherCondition(dayAndConditionParts[1].trim());
                }
            }

            // Extracting weather details from the description
            String[] descriptionParts = description.split(", ");
            for (String part : descriptionParts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    switch (key) {
                        case "Temperature":
                            observation.setTemperature(value);
                            break;
                        case "Humidity":
                            observation.setHumidity(value);
                            break;
                        case "Pressure":
                            observation.setPressure(value);
                            break;
                        case "Wind Direction":
                            observation.setWindDirection(value);
                            break;
                        case "Wind Speed":
                            observation.setWindSpeed(value);
                            break;
                        case "Visibility":
                            observation.setVisibility(value);
                            break;
                        default:
                            break;
                    }
                }
            }

            // Map weather condition to image resource
            int imageResource = mapWeatherConditionToImageResource(observation.getWeatherCondition());
            observation.setImageResource(imageResource);

            // Setting the pubDate
            observation.setDate(pubDate);
        }

        return observation;
    }




}
