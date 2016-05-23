package it.ennova.rxadvertise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.ennova.zerxconf.ZeRXconf;
import it.ennova.zerxconf.model.NetworkServiceDiscoveryInfo;
import it.ennova.zerxconf.utils.NsdUtils;
import rx.Subscription;
import rx.functions.Action1;

public class DetailActivity extends AppCompatActivity {

    private static final String SERVICE_INFO = DetailActivity.class.getSimpleName() + "SERVICE_INFO";
    private static final String SERVICE_RESOLVED = DetailActivity.class.getSimpleName() + "SERVICE_RESOLVED";

    public static Intent getDetailActivityIntent(@NonNull Activity parent,
                                                 @NonNull NetworkServiceDiscoveryInfo info,
                                                 boolean resolved) {

        Intent intent = new Intent(parent, DetailActivity.class);
        intent.putExtra(SERVICE_INFO, info);
        intent.putExtra(SERVICE_RESOLVED, resolved);

        return intent;
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.detailedInfo)
    TextView txtInfo;

    private Subscription subscription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NetworkServiceDiscoveryInfo info = getIntent().getParcelableExtra(SERVICE_INFO);
        if (getIntent().getBooleanExtra(SERVICE_RESOLVED, false)) {
            showData(info);
        } else {
            subscription = ZeRXconf.startDiscovery(this, NsdUtils.cleanProtocolOf(info), true).subscribe(onServiceResolved, onError);
        }
    }

    private void showData(NetworkServiceDiscoveryInfo info) {
        setTitle(info.getServiceName());
        txtInfo.setText(info.toString());
    }

    private final Action1<NetworkServiceDiscoveryInfo> onServiceResolved = new Action1<NetworkServiceDiscoveryInfo>() {
        @Override
        public void call(NetworkServiceDiscoveryInfo info) {
            showData(info);
        }
    };

    private Action1<Throwable> onError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            warnUser(throwable);
        }
    };

    private void warnUser(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
