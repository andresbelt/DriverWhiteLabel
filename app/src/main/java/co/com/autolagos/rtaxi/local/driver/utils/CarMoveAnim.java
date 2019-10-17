package co.com.autolagos.rtaxi.local.driver.utils;



import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.CameraPosition.Builder;


public class CarMoveAnim {
    public CarMoveAnim() {
    }

    public static void startcarAnimation(final Marker carMarker, final GoogleMap googleMap, final LatLng startPosition, final LatLng endPosition, int duration, final CancelableCallback callback) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        if (duration == 0 || duration < 3000) {
            duration = 3000;
        }

        valueAnimator.setDuration((long)duration);
        final CarMoveAnim.LatLngInterpolatorNew latLngInterpolator = new CarMoveAnim.LatLngInterpolatorNew.LinearFixed();
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = valueAnimator.getAnimatedFraction();
                double var10000 = (double)v * endPosition.longitude + (double)(1.0F - v) * startPosition.longitude;
                var10000 = (double)v * endPosition.latitude + (double)(1.0F - v) * startPosition.latitude;
                LatLng newPos = latLngInterpolator.interpolate(v, startPosition, endPosition);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5F, 0.5F);
                carMarker.setRotation((float)CarMoveAnim.bearingBetweenLocations(startPosition, endPosition));
                if (callback != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition((new Builder()).target(newPos).bearing((float)CarMoveAnim.bearingBetweenLocations(startPosition, endPosition)).zoom(18.0F).build()), callback);
                } else {
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition((new Builder()).target(newPos).bearing((float)CarMoveAnim.bearingBetweenLocations(startPosition, endPosition)).zoom(18.0F).build()));
                }

            }
        });
        valueAnimator.start();
    }

    private static double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
        double PI = 3.14159D;
        double lat1 = latLng1.latitude * PI / 180.0D;
        double long1 = latLng1.longitude * PI / 180.0D;
        double lat2 = latLng2.latitude * PI / 180.0D;
        double long2 = latLng2.longitude * PI / 180.0D;
        double dLon = long2 - long1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360.0D) % 360.0D;
        return brng;
    }

    public interface LatLngInterpolatorNew {
        LatLng interpolate(float var1, LatLng var2, LatLng var3);

        public static class LinearFixed implements CarMoveAnim.LatLngInterpolatorNew {
            public LinearFixed() {
            }

            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * (double) fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                if (Math.abs(lngDelta) > 180.0D) {
                    lngDelta -= Math.signum(lngDelta) * 360.0D;
                }

                double lng = lngDelta * (double) fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

}