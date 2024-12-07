package com.example.secondapp;

public class WeatherResponse {
    public Main main;

    public static class Main {
        public float pressure;
        public float temp;
    }
}
