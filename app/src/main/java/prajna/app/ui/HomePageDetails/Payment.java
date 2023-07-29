package prajna.app.ui.HomePageDetails;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.atom.sdk.AtomConfiguration;
import com.atom.sdk.AtomPayment;
import com.atom.sdk.OnPaymentResponseListener;
import com.atom.sdk.model.common.CustDetails;
import com.atom.sdk.model.request.PaymentData;

import prajna.app.R;

public class Payment extends AppCompatActivity  {

    AtomPayment atom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

    }


}
