/*
package com.congnt.emergencyassistance;

*
 * Created by congnt24 on 08/12/2016.



public class Test {
    public void onLocationChanged(Location paramLocation)
    {
        if ((paramLocation.hasAccuracy()) && (paramLocation.getAccuracy() > 400.0F)) {
            return;
        }
        LocationData localLocationData;
        float f1;
        if (paramLocation.hasSpeed())
        {
            localLocationData = new LocationData(this.f, paramLocation.getLongitude(), paramLocation.getLatitude(), paramLocation.getSpeed(), paramLocation.getAccuracy());
            this.w = paramLocation.getTime();
            if (paramLocation.hasSpeed()) {
                this.c.a(localLocationData);
            }
            this.g = Math.max(this.g, paramLocation.getSpeed());
            f1 = paramLocation.getAccuracy();
            if (f1 < 50.0F) {
                break label206;
            }
            this.y = 1;
            label112:
            if (paramLocation.hasAltitude()) {
                this.t.a(paramLocation.getAltitude());
            }
            if (this.u == null) {
                break label278;
            }
            f1 = paramLocation.distanceTo(this.u);
            if (f1 > 10.0F) {
                this.v = (f1 + this.v);
            }
        }
        label206:
        label278:
        for (this.u = paramLocation;; this.u = paramLocation)
        {
            a(localLocationData);
            if ((m() == null) || (!b(e()))) {
                break;
            }
            m().a(paramLocation, e());
            return;
            if (f1 >= 30.0F)
            {
                this.y = 2;
                break label112;
            }
            if (f1 >= 15.0F)
            {
                this.y = 3;
                break label112;
            }
            if (f1 >= 10.0F)
            {
                this.y = 4;
                break label112;
            }
            if (f1 >= 0.001F)
            {
                this.y = 5;
                break label112;
            }
            this.y = 1;
            break label112;
        }
    }

    public void onSensorChanged(SensorEvent paramSensorEvent)
    {
        Object localObject = null;
        this.f = paramSensorEvent.timestamp;
        long l1;
        if ((paramSensorEvent.sensor.getType() == 1) || (paramSensorEvent.sensor.getType() == 10))
        {
            paramSensorEvent = new AccelerationData(this.f, paramSensorEvent.values[0], paramSensorEvent.values[1], paramSensorEvent.values[2]);
            if (this.h != 0L)
            {
                l1 = this.f - this.h;
                this.j = Math.max(this.j, l1);
                if (l1 > 150000000L) {
                    this.F += 1;
                }
            }
            if ((this.j > 100000000L) && (this.G < this.j))
            {
                this.p.a(new l("SensorData", "Accel", this.j / 1000L / 1000L).a());
                this.G = this.j;
            }
            this.h = this.f;
            this.I = true;
        }

        for (;;)
        {
            if ((this.u != null) && (System.currentTimeMillis() - this.w > 5000L)) {
                this.y = 0;
            }
            this.c.a(paramSensorEvent);
            if (this.x) {
                a(paramSensorEvent);
            }
            if ((!this.K) && (this.I) && ((this.J) || (!this.o)))
            {
                this.K = true;
                v();
            }
            return;
            if (paramSensorEvent.sensor.getType() == 4)
            {
                paramSensorEvent = new RotationData(this.f, paramSensorEvent.values[0], paramSensorEvent.values[1], paramSensorEvent.values[2]);
                if (this.i != 0L)
                {
                    l1 = this.f - this.i;
                    this.k = Math.max(this.k, l1);
                    if ((l1 > 150000000L) && (n())) {
                        this.F += 1;
                    }
                }
                this.i = this.f;
                this.J = true;
            }
            else
            {
                b.warn("Unkown sensor event raised.");
                paramSensorEvent = (SensorEvent)localObject;
            }
        }
    }

}
*/
