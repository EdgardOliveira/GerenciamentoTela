package br.com.technologies.venom;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class DeviceAdmin extends DeviceAdminReceiver {

    public void onEnabled(Context context, Intent intent){
        super.onEnabled(context, intent);
        Toast.makeText(context, "Habilitado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, "Desabilitado", Toast.LENGTH_SHORT).show();
    }
}
