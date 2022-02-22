package site.alexkononsol.controllerfortelegrambot.ui.fragments;

import static java.lang.String.format;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import site.alexkononsol.controllerfortelegrambot.R;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.ServerResponse;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestToServer;
import site.alexkononsol.controllerfortelegrambot.connectionsUtils.requests.RequestType;
import site.alexkononsol.controllerfortelegrambot.utils.Constants;
import site.alexkononsol.controllerfortelegrambot.utils.SettingsManager;

public class AboutProgramFragment extends Fragment {
    private View view;
    private boolean isUpdate;
    private Button updateButton;
    TextView contentView;
    String newVersion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_program, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        view = getView();
        viewInfoAboutVersionApp();
        updateButton = (Button) view.findViewById(R.id.about_fragment_button);
        contentView = view.findViewById(R.id.about_fragment_update_textView);
        contentView.setText(getString(R.string.about_fragment_update_textView));
        if (isUpdate) {
            updateButton.setText(getString(R.string.about_fragment_button_download));
        } else {
            updateButton.setText(getString(R.string.about_fragment_button_update));
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.setText(getString(R.string.toastLoading));
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            if (isUpdate) download();
                            else update();

                        } catch (Exception ex) {
                            contentView.post(new Runnable() {
                                public void run() {
                                    contentView.setText(getString(R.string.error) + ex.getMessage() + ex.getLocalizedMessage());
                                    Log.d("error",ex.getMessage(),ex);
                                    Toast.makeText(getContext(), getString(R.string.error) + " : ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();

            }

        });
    }

    private void viewInfoAboutVersionApp() {
        TextView versionView = getView().findViewById(R.id.about_fragment_version_title);
        try {
            String version = getCurrentVersion();
            versionView.setText(format(getString(R.string.version_app), version));

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ERROR", "don't viewed version app", e);
            e.printStackTrace();
        }
    }

    private String getCurrentVersion() throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        return pInfo.versionName;
    }
    private void update() throws PackageManager.NameNotFoundException {
        RequestToServer update = new RequestToServer(Constants.ENDPOINT_UPDATE, RequestType.GET);
        update.addParam("version", getCurrentVersion());
        ServerResponse response = update.send();
        if (response.getCode()==200){
            isUpdate = true;
            newVersion = response.getData();
            updateButton.post(new Runnable() {
                @Override
                public void run() {
                    updateButton.setText(getString(R.string.about_fragment_button_download));
                }
            });
            contentView.post(new Runnable() {
                public void run() {
                    String newVersionExist = format(getString(R.string.about_fragment_new_version_exist),newVersion);
                    contentView.setText(newVersionExist);
                }
            });
        }else {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    contentView.setText(getString(R.string.about_fragment_no_update));
                }
            });
        }
    }
    private void download(){
        DownloadManager downloadmanager = (DownloadManager)  getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(SettingsManager.getSettings().getHostName()+Constants.ENDPOINT_DOWNLOAD_APP);
        String fileName = String.format("ControllerForTelegramBot v-%s.apk",newVersion.trim());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription(String.format(getString(R.string.about_fragment_download_description),fileName));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
        downloadmanager.enqueue(request);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.setText(getString(R.string.about_fragment_download_view));
            }
        });
    }
}