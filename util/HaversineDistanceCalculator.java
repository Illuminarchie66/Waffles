package uk.ac.warwick.cs126.util;

import java.lang.Math;

public class HaversineDistanceCalculator {

    private final static float R = 6372.8f;
    private final static float kilometresInAMile = 1.609344f;

    public static float haversine(float theta) {
        return (float) Math.pow(Math.sin(theta/2), 2);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private static float based(float lat1, float lon1, float lat2, float lon2) {
        float phi1 = (float) (lat1*(Math.PI / 180));
        float phi2 = (float) (lat2*(Math.PI / 180));
        float lambda1 = (float) (lon1*(Math.PI / 180));
        float lambda2 = (float) (lon2*(Math.PI / 180));

        float alpha = (float) (haversine(phi2-phi1) + Math.cos(phi1) * Math.cos(phi2) * haversine(lambda2-lambda1));
        float d = (float) (R * 2 * Math.asin(Math.sqrt(alpha)));
        return d;
    }

    public static float inKilometres(float lat1, float lon1, float lat2, float lon2) {
        float d = (float) round(based(lat1, lon1, lat2, lon2),1);
        return d;
    }

    public static float inMiles(float lat1, float lon1, float lat2, float lon2) {
        float d = (float) round((based(lat1, lon1, lat2, lon2) / kilometresInAMile), 1);
        return d;
    }

    public void he() {
        System.out.println("based");
    }
}