package br.com.technologies.venom;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LIGAR_TELA = 1;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Button btnOn, btnOff;
    private boolean ativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOff = findViewById(R.id.btnOff);
        btnOn = findViewById(R.id.btnOn);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, DeviceAdmin.class);
        ativo = devicePolicyManager.isAdminActive(componentName);

        if (ativo) {
            btnOn.setText("Desativar tela");
            btnOff.setVisibility(View.VISIBLE);
        } else {
            btnOn.setText("Ativar tela");
            btnOff.setVisibility(View.GONE);
        }

        btnOn.setOnClickListener(this);
        btnOff.setOnClickListener(this);
    }

    private void ligarTela(View view) {
        ativo = devicePolicyManager.isAdminActive(componentName);

        if (ativo) {
            devicePolicyManager.removeActiveAdmin(componentName);
            btnOn.setText("Ativar tela");
            btnOff.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Você deve habilitar o app!");
            startActivityForResult(intent, LIGAR_TELA);
        }
    }

    private void desligarTela(View view) {
        devicePolicyManager.lockNow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LIGAR_TELA:
                if (resultCode == RESULT_OK) {
                    btnOn.setText("Desativar tela");
                    btnOff.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(
                            getApplicationContext(), "Falhou!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOn:
                ligarTela(v);
                break;
            case R.id.btnOff:
                desligarTela(v);
                break;
        }
    }
}