package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static java.lang.String.format;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RetrofitRequestType;
import site.alexkononsol.controllerfortelegrambot.logHelper.LogHelper;
import site.alexkononsol.controllerfortelegrambot.utils.ApkInstaller;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class AboutProgramFragment extends Fragment {
    private boolean isUpdate;
    private Button updateButton;
    TextView contentView;
    String newVersion;
    private SwitchCompat autoInstall;
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_program, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        viewInfoAboutVersionApp();
        autoInstall = getView().findViewById(R.id.about_fragment_switch);
        updateButton = (Button) getView().findViewById(R.id.about_fragment_button);
        contentView = getView().findViewById(R.id.about_fragment_update_textView);
        contentView.setText(getString(R.string.about_fragment_update_textView));
        if (isUpdate) {
            updateButton.setText(getString(R.string.about_fragment_button_download));
        } else {
            updateButton.setText(getString(R.string.about_fragment_button_update));
        }
        updateButton.setOnClickListener(v -> {
            contentView.setText(getString(R.string.toastLoading));
            new Thread(() -> {
                try {
                    if (isUpdate) download();
                    else update();

                } catch (Exception ex) {
                    contentView.post(() -> {
                        contentView.setText(new StringBuilder().append(getString(R.string.error)).append(ex.getMessage()).append(ex.getLocalizedMessage()).toString());
                        LogHelper.logError(AboutProgramFragment.this, ex.getMessage(), ex);
                        Toast.makeText(getContext(), getString(R.string.error) + " : ", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();

        });
        autoInstall.setOnClickListener(view -> SettingsManager.getSettings().setAutoInstall(autoInstall.isChecked()));

    }

    private void viewInfoAboutVersionApp() {
        TextView versionView = getView().findViewById(R.id.about_fragment_version_title);
        String version = getCurrentVersion();
        versionView.setText(format(getString(R.string.version_app), version));
    }

    private String getCurrentVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }

    private void update() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        AtomicReference<ServerResponse> response = new AtomicReference<>();
        executor.execute(() -> {
            String param = getCurrentVersion();
            RetrofitRequestToServer requestToServer = new RetrofitRequestToServer();
            response.set(requestToServer.stringRequest(param, RetrofitRequestType.UPDATE));
            handler.post(() -> {
                //UI Thread work here
                if (response.get().getCode() == 200) {
                    isUpdate = true;
                    newVersion = response.get().getData().toString();
                    updateButton.setText(getString(R.string.about_fragment_button_download));
                    String newVersionExist = format(getString(R.string.about_fragment_new_version_exist), newVersion);
                    contentView.setText(newVersionExist);
                } else contentView.setText(getString(R.string.about_fragment_no_update));
            });
        });
    }

    private void download() {
        DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(SettingsManager.getSettings().getHostName() + Constants.ENDPOINT_DOWNLOAD_APP);
        String fileName = String.format("ControllerForTelegramBot v-%s.apk", newVersion.trim());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription(String.format(getString(R.string.about_fragment_download_description), fileName));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        downloadmanager.enqueue(request);
        contentView.post(() -> contentView.setText(getString(R.string.about_fragment_download_view)));
        path = Environment.getExternalStorageDirectory().getPath() + "/Download/" + fileName;
        LogHelper.logDebug(this, "download file path is " + path);
    }

    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (autoInstall.isChecked()) ApkInstaller.installApplication(getContext(), path);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(broadcast, intentFilter);
        autoInstall.setChecked(SettingsManager.getSettings().isAutoInstall());
    }
}