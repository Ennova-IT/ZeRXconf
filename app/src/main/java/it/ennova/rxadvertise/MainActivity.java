package it.ennova.rxadvertise;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Subscription s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnNsdService).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (s == null || s.isUnsubscribed()) {
            s = NsdObservable.advertise(this, "NSD Service", "_http._tcp.", 888)
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
