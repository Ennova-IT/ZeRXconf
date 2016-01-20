package it.ennova.rxadvertise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    Subscription s;

    private static final String SERVICE_NAME = "Skillo";
    private static final String SERVICE_LAYER = "_http._tcp.";
    private static final int SERVICE_PORT = 888;
    private Map<String, String> attributes = new HashMap<>(1);
    private final Map<String, String> emptyAttributes = new HashMap<>(0);
    private boolean forceNative = false;
    private boolean forceEmptySet = false;

    private ImageView imgAdvertiseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnAdvertiseService).setOnClickListener(this);
        ((Switch)findViewById(R.id.switchNativeApi)).setOnCheckedChangeListener(this);
        ((Switch)findViewById(R.id.switchEmptyAttributeSet)).setOnCheckedChangeListener(this);
        imgAdvertiseStatus = (ImageView) findViewById(R.id.imgAdvertiseStatus);

        attributes.put("canConfigure", "NO");
    }

    @Override
    public void onClick(View v) {
        if (s == null || s.isUnsubscribed()) {
            s = ZeRXconf.advertise(this, SERVICE_NAME, SERVICE_LAYER, SERVICE_PORT, getAttributes(), forceNative)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, onError);
        } else {
            s.unsubscribe();
            imgAdvertiseStatus.setImageResource(R.drawable.ic_remove_circle_red_500_18dp);
        }

    }

    private Map<String, String> getAttributes() {
        return forceEmptySet ? emptyAttributes : attributes;
    }

    private Action1<NetworkServiceDiscoveryInfo> onNext = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo networkServiceDiscoveryInfo) {
            imgAdvertiseStatus.setImageResource(R.drawable.ic_check_circle_green_500_18dp);
        }
    };

    private Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            makeMessage(throwable.getMessage());
        }
    };

    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.switchNativeApi) {
            forceNative = isChecked;
        } else {
            forceEmptySet = isChecked;
        }
    }
}
