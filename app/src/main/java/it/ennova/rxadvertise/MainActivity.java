package it.ennova.rxadvertise;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import it.ennova.zerxconf.JBNsdObservable;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Subscription s;

    private static final String SERVICE_NAME = "NSD Service";
    private static final String SERVICE_LAYER = "_http._tcp.";
    private static final int SERVICE_PORT = 888;
    private Map<String, String> attributes = new HashMap<>(1);

    JmDNS jmDNS = null;
    ServiceInfo serviceInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnNsdService).setOnClickListener(this);

        attributes.put("path", "index.html");
    }

    @Override
    public void onClick(View v) {

        startCompatNdsAdvertising();

    }

    private void startCompatNdsAdvertising() {
        Thread t = new Thread() {
            @Override
            public void run() {
                startCompatNdsAdvertisingOnNewThread();
            }
        };

        t.start();
    }

    private void startCompatNdsAdvertisingOnNewThread() {
        if (serviceInfo == null) {
            try {
                jmDNS = JmDNS.create();
                serviceInfo = ServiceInfo.create(SERVICE_LAYER, SERVICE_NAME, SERVICE_PORT, 0, 0, attributes);
                jmDNS.registerService(serviceInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            jmDNS.unregisterService(serviceInfo);
            jmDNS.unregisterAllServices();
            serviceInfo = null;
        }
    }

    private void startJBNdsAdvertising() {
        if (s == null || s.isUnsubscribed()) {
            s = JBNsdObservable.advertise(this, SERVICE_NAME, SERVICE_LAYER, SERVICE_PORT, attributes)
                    .subscribe(new Action1<NsdServiceInfo>() {
                        @Override
                        public void call(NsdServiceInfo nsdServiceInfo) {
                            makeMessage("Servizio " + nsdServiceInfo.getServiceName());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            makeMessage(throwable.getMessage());
                        }
                    });
        } else {
            s.unsubscribe();
        }
    }

    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
