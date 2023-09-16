package com.example.practic_guiada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.net.ConnectivityManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Activity activity;
    //version adnroid
    private TextView versionAndroid;
    private int versionSDK;
    //Bateria
    private ProgressBar pbLevelBaterry;
    IntentFilter baterryFiler;
    private TextView tvLevelBaterry;
    //camara
    CameraManager cameraManager;
    String cameraId;
    private Button btnonLigth;
    private Button btnofLigth;
    //Archivo
    private EditText etNameFile;
    private Archivo archivo;
    private ImageButton btnCreateFile;
    //Conexion
    private  TextView tvConexion;
    ConnectivityManager conexion;
    // Bluetooth
    private Button btnOnBluetooth;
    private Button btnOfBluetooth;
    private BluetoothAdapter bluetoothAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        begin();
        baterryFiler= new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, baterryFiler);
        this.btnonLigth.setOnClickListener(this :: onLinght);
        this.btnofLigth.setOnClickListener(this :: offLinght);
        archivo = new Archivo(context, activity);
        btnCreateFile.setOnClickListener(this::saveFile);
        this.btnOnBluetooth.setOnClickListener(this::onBluetooth);
        this.btnOfBluetooth.setOnClickListener(this::ofBluetooth);
    }

    private void begin(){
        this.context = getApplicationContext();
        this.activity = this;
        this.versionAndroid = findViewById(R.id.tvVersionAndroid);
        this.pbLevelBaterry = findViewById(R.id.pdLevelBaterry);
        this.tvLevelBaterry = findViewById(R.id.tvLevelBaterryLB);
        this.etNameFile = findViewById(R.id.etNameFile);
        this.tvConexion = findViewById(R.id.tvConection);
        this.btnonLigth = findViewById(R.id.btnOn);
        this.btnofLigth = findViewById(R.id.btnOf);
        this.etNameFile = findViewById(R.id.etNameFile);
        this.btnCreateFile = findViewById(R.id.btnSaveFile);
        this.btnOfBluetooth = findViewById(R.id.btnOfBluetooth);
        this.btnOnBluetooth = findViewById(R.id.btnOnBluetooth);

    }
    //SO version

    @Override
    protected void onResume() {
        super.onResume();
        String versionSO = Build.VERSION.RELEASE;
        versionSDK = Build.VERSION.SDK_INT;
        versionAndroid.setText("Version SO:"+versionSO+" /SDK:"+versionSDK);
        checkConnection();
    }
    //Linterna flash
    private void onLinght(View view)  {
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
            Toast.makeText(context, ""+cameraId, Toast.LENGTH_SHORT).show();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

    }
    private void offLinght(View view){
        try {
            cameraManager.setTorchMode(cameraId,false);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Bateria
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            pbLevelBaterry.setProgress(levelBattery);
            tvLevelBaterry.setText("Nivel de la batería: " + levelBattery + " %");
        }
    };

    // Conexión
    private void checkConnection() {
        conexion = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conexion.getActiveNetworkInfo();
        boolean stateNet = network != null && network.isConnectedOrConnecting();
        if (stateNet) tvConexion.setText("Sí hay conexión a Internet");
        else tvConexion.setText("No hay conexión a Internet");
    }
    // Archivo
    private void saveFile(View view) {
        String nameFile = etNameFile.getText().toString();
        if (nameFile.isEmpty()) {
            Toast.makeText(context, "Especifique un nombre para guardar el archivo", Toast.LENGTH_LONG).show();
        } else {
            archivo.saveFile(nameFile, " ");
        }
    }

    // Bluetooth
    public void onBluetooth(View view) {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 100);
        }

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }
    public void ofBluetooth(View view) {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
        }

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }
}