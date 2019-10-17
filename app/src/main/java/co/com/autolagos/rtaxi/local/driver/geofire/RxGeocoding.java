package co.com.autolagos.rtaxi.local.driver.geofire;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

import io.reactivex.Maybe;

public final class RxGeocoding {

    public static Maybe<List<Address>> geocoding(@NonNull Context context, String locationName, int maxResults) {
        return geocodingBuilder(context)
                .locationName(locationName)
                .maxResults(maxResults)
                .build();
    }

    public static Maybe<List<Address>> geocoding(@NonNull Context context, String locationName) {
        return geocodingBuilder(context)
                .locationName(locationName)
                .build();
    }

    public static Maybe<List<Address>> geocodingReverse(@NonNull Context context, double latitude, double longitude) {
        return geocodingReverseBuilder(context)
                .location(latitude, longitude)
                .build();
    }

    public static Maybe<List<Address>> geocodingReverse(@NonNull Context context, double latitude, double longitude, int maxResults) {
        return geocodingReverseBuilder(context)
                .location(latitude, longitude)
                .maxResults(maxResults)
                .build();
    }


    public static GeocodingBuilder geocodingBuilder(@NonNull Context context) {
        return new GeocodingBuilder(context);
    }

    public static GeocodingReverseBuilder geocodingReverseBuilder(@NonNull Context context) {
        return new GeocodingReverseBuilder(context);
    }

    private static Geocoder getGeocoder(Context context, Locale locale) {
        if (locale != null) return new Geocoder(context, locale);
        return new Geocoder(context);
    }

    public static class GeocodingBuilder {

        private final Context context;
        private Locale locale;
        private String locationName;
        private int maxResults;
        private LatLngBounds latLngBounds;

        GeocodingBuilder(@NonNull Context context) {
            this.context = context;
        }

        public Maybe<List<Address>> build() {
            return Maybe.fromCallable(() -> {
                Geocoder geocoder = getGeocoder(context, locale);
                if (latLngBounds != null) {
                    double lowerLeftLatitude = latLngBounds.southwest.latitude;
                    double lowerLeftLongitude = latLngBounds.southwest.longitude;
                    double upperRightLatitude = latLngBounds.northeast.latitude;
                    double upperRightLongitude = latLngBounds.northeast.longitude;
                    return geocoder.getFromLocationName(
                            locationName, maxResults,
                            lowerLeftLatitude, lowerLeftLongitude,
                            upperRightLatitude, upperRightLongitude);
                } else {
                    return geocoder.getFromLocationName(locationName, maxResults);
                }
            });
        }

        public GeocodingBuilder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public GeocodingBuilder maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public GeocodingBuilder latLngBounds(LatLngBounds latLngBounds) {
            this.latLngBounds = latLngBounds;
            return this;
        }

        public GeocodingBuilder latLngBounds(double lowerLeftLatitude, double lowerLeftLongitude,
                                             double upperRightLatitude, double upperRightLongitude) {
            LatLng soutWest = new LatLng(lowerLeftLatitude, lowerLeftLongitude);
            LatLng northEast = new LatLng(upperRightLatitude, upperRightLongitude);
            latLngBounds = new LatLngBounds(soutWest, northEast);
            return this;
        }

        public GeocodingBuilder locale(Locale locale) {
            this.locale = locale;
            return this;
        }
    }

    public static class GeocodingReverseBuilder {

        private final Context context;
        private Locale locale;
        private double latitude;
        private double longitude;
        private int maxResults;

        GeocodingReverseBuilder(@NonNull Context context) {
            this.context = context;
        }

        public Maybe<List<Address>> build() {
            return Maybe.fromCallable(() -> getGeocoder(context, locale).getFromLocation(latitude, longitude, maxResults));
        }

        public GeocodingReverseBuilder location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        public GeocodingReverseBuilder maxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public GeocodingReverseBuilder locale(Locale locale) {
            this.locale = locale;
            return this;
        }
    }
}