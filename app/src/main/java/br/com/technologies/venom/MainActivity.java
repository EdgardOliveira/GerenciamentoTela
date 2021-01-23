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

    private static final int LIGAR_TELA = 10001;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Button btnGerenciar, btnDesligar;
    private boolean ativo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associando componentes à variáveis
        btnDesligar = findViewById(R.id.btnDesligar);
        btnGerenciar = findViewById(R.id.btnGerenciar);

        gerenciarTela();

        //Escutando por eventos de click
        btnGerenciar.setOnClickListener(this);
        btnDesligar.setOnClickListener(this);
    }

    /**
     * Objetivo: Verificar e Fazer o gerenciamento da tela pelo app de acordo com a vontade do usuário
     */
    private void gerenciarTela() {
        //Configurando o gerenciador de diretivas do dispositivo
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, DeviceAdmin.class);
        //Verifica o status do gerenciamento da tela
        ativo = devicePolicyManager.isAdminActive(componentName);

        if (ativo) {
            removerDireitoGerenciamentoTela();
        } else {
            adicionarDireitoGerenciamentoTela();
        }
    }

    private void removerDireitoGerenciamentoTela() {
        //atualmente está permitido o gerenciamento pelo app... então irá remover a permissão
        devicePolicyManager.removeActiveAdmin(componentName);
        //configura o botão de gerenciamento para exibir Permitir... e esconde o desligar
        configurarBtnGerenciamento(false);
    }

    private void configurarBtnGerenciamento(boolean status) {
        if (status) {
            //permitiu o gerenciamento pelo app
            btnGerenciar.setText("Desabilitar o gerenciamento da tela");
            //Exibe o botão para permitir desligar a tela
            btnDesligar.setVisibility(View.VISIBLE);
        } else {
            //Não está permitido o gerenciamento pelo app
            btnGerenciar.setText("Habilitar o gerenciamento da tela");
            //Esconde o botão de desligar a tela
            btnDesligar.setVisibility(View.GONE);
        }

    }

    private void adicionarDireitoGerenciamentoTela() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Você deve permitir o gerenciamento da tela pelo app!");
        startActivityForResult(intent, LIGAR_TELA);

        configurarBtnGerenciamento(true);
    }

    private void desligarTela() {
        devicePolicyManager.lockNow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LIGAR_TELA:
                if (resultCode == RESULT_OK) {
                    configurarBtnGerenciamento(true);
                } else {
                    Toast.makeText(
                            getApplicationContext(), "Falhou ao tentar gerenciar a tela!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGerenciar:
                gerenciarTela();
                break;
            case R.id.btnDesligar:
                desligarTela();
                break;
        }
    }
}