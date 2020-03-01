package rokolabs.com.peoplefirst.repository;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.HttpException;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.model.FileUrl;
import rokolabs.com.peoplefirst.model.Report;
import rokolabs.com.peoplefirst.model.RetailReport;
import rokolabs.com.peoplefirst.model.RetailUser;
import rokolabs.com.peoplefirst.model.RetailWitnessTestimony;
import rokolabs.com.peoplefirst.model.TransgressorReport;
import rokolabs.com.peoplefirst.model.User;
import rokolabs.com.peoplefirst.model.VictimTestimony;
import rokolabs.com.peoplefirst.model.WitnessTestimony;
import rokolabs.com.peoplefirst.model.responses.BaseResponse;
import rokolabs.com.peoplefirst.utils.Utils;

public class HarassmentRepository {

    public final static int WITNESS = 0;
    public final static int TRANSGRESSOR = 1;
    public final static int VICTIM = 2;
    public final static int EMPTY = -1;

    public int named = EMPTY;

    private PeopleFirstService mService;
    private Context mContext;

    public BehaviorSubject<ArrayList<Report>> myOpenReports = BehaviorSubject.create();
    public BehaviorSubject<ArrayList<Report>> myClosedResolvedReports = BehaviorSubject.create();
    public BehaviorSubject<Report> currentReport = BehaviorSubject.create();
    public BehaviorSubject<User> me = BehaviorSubject.create();

    public BehaviorSubject<WitnessTestimony> currentWitnessTestimony = BehaviorSubject.create();
    public BehaviorSubject<TransgressorReport> currentTransgressorReport = BehaviorSubject.create();
    public BehaviorSubject<VictimTestimony> currentVictimTestimony = BehaviorSubject.create();

    public ArrayList<User> mSelectedUsersForWitnessOrAggressorReport = new ArrayList<>();

    public ArrayList<UrlSender> urlSenders = new ArrayList<>();

    public boolean profileForCreateReport = false;

    @Inject
    public HarassmentRepository(PeopleFirstService service, Context context) {
        mService = service;
        mContext = context;
        if (!"".equals(Utils.getPermanentValue("x-access-token", mContext))) {
            getMyReports();
        }
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null) return;
                int id = intent.getIntExtra("reportId", 0);
                String type = intent.getStringExtra("type");
                for (UrlSender sender : urlSenders) {
                    if (!sender.hasReportId()) {
                        sender.setReportId(id);
                        sender.setType(type);
                        sender.trySendingUrl();
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter("REPORT_ADDED"));
    }


    public void addReport(Report report) {
        if (me.getValue().retail==1) {
            RetailReport rr = report.convertToRetail();
            RetailUser rme = me.getValue().convertToRetail();
            if (rr.victim != null) {
                if (!rr.victim.equals(rme)) {
                    if (!rr.witnesses.contains(rme)) {
                        rr.witnesses.add(rme);
                    }
                }
            }
            mService.addRetailReport(report.convertToRetail())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reportResponse -> {
                        if (reportResponse.success) {
                            getMyReports();
                            currentReport.onNext(reportResponse.data);
                            Intent intent = new Intent("REPORT_ADDED");
                            intent.putExtra("reportId", reportResponse.data.id);
                            intent.putExtra("type", "initial");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } else
                            Toast.makeText(mContext, reportResponse.error.message, Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        if(throwable instanceof HttpException){
                            HttpException exception= (HttpException) throwable;
                            String s=exception.response().errorBody().string();
                            String t=s;
                            t=t+"asd";
                        }
                        Toast.makeText(mContext, "Could not add report", Toast.LENGTH_LONG).show();

                    });
        } else {
            if (report.victim != null) {//жертва указана
                if (!report.victim.equals(me.getValue())) {//репорт составлен на жертву другого человека
                    if (!report.witnesses.contains(me.getValue())) {
                        report.witnesses.add(me.getValue());
                    }
                }
            }
            mService.addReport(report)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(reportResponse -> {
                        if (reportResponse.success) {
                            getMyReports();
                            currentReport.onNext(reportResponse.data);
                            Intent intent = new Intent("REPORT_ADDED");
                            intent.putExtra("reportId", reportResponse.data.id);
                            intent.putExtra("type", "initial");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } else
                            Toast.makeText(mContext, reportResponse.error.message, Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        Toast.makeText(mContext, "Could not add report", Toast.LENGTH_LONG).show();
                    });
        }
    }

    public void addWitnessReport() {
        if (me.getValue().retail==1) {
            RetailWitnessTestimony rwr = currentWitnessTestimony.getValue().convertToRetail();
            mService.addRetailWitnessTestimony(currentReport.getValue().id, rwr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(baseResponse -> {
                        if (baseResponse.success) {
                            getMyReports();
                            Intent intent = new Intent("REPORT_ADDED");
                            intent.putExtra("reportId", baseResponse.data.id);
                            intent.putExtra("type", "witness");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } else
                            Toast.makeText(mContext, baseResponse.error.message, Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        Toast.makeText(mContext, "Could not verify report", Toast.LENGTH_LONG).show();
                    });
        } else {
            mService.addWitnessTestimony(currentReport.getValue().id, currentWitnessTestimony.getValue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(baseResponse -> {
                        if (baseResponse.success) {
                            getMyReports();
                            Intent intent = new Intent("REPORT_ADDED");
                            intent.putExtra("reportId", baseResponse.data.id);
                            intent.putExtra("type", "witness");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        } else
                            Toast.makeText(mContext, baseResponse.error.message, Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        Toast.makeText(mContext, "Could not verify report", Toast.LENGTH_LONG).show();
                    });
        }
    }

    public void addTransgressorReport() {
        mService.addTransgressorReport(currentReport.getValue().id, currentTransgressorReport.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseResponse -> {
                    if (baseResponse.success) {
                        getMyReports();
                        Intent intent = new Intent("REPORT_ADDED");
                        intent.putExtra("reportId", baseResponse.data.id);
                        intent.putExtra("type", "aggressor");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    } else
                        Toast.makeText(mContext, baseResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    HttpException e = (HttpException) throwable;
                    if (e.code() == 400) {
                        //report already exists
                        mService.updateTransgressorReport(currentTransgressorReport.getValue().id, currentTransgressorReport.getValue())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(baseResponse -> {
                                    if (baseResponse.success)
                                        getMyReports();
                                    else
                                        Toast.makeText(mContext, baseResponse.error.message, Toast.LENGTH_LONG).show();
                                }, throwable1 -> {
                                    Toast.makeText(mContext, "Could not verify report", Toast.LENGTH_LONG).show();
                                });
                    } else {
                        Toast.makeText(mContext, "Could not verify report", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void addVicitimTestimony() {
        mService.addVictimTestimony(currentReport.getValue().id, currentVictimTestimony.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseResponse -> {
                    if (baseResponse.success) {
                        getMyReports();
                    } else
                        Toast.makeText(mContext, baseResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    HttpException e = (HttpException) throwable;
                    Toast.makeText(mContext, "Could not verify report", Toast.LENGTH_LONG).show();
                });
    }

    public void getReports() {
        mService.getReports()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportsResponse -> {
                    if (reportsResponse.success)
                        myOpenReports.onNext(reportsResponse.data);
                    else
                        Toast.makeText(mContext, reportsResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not get myOpenReports", Toast.LENGTH_LONG).show();
                });
    }

    @SuppressLint("CheckResult")
    public void getMyReports() {
        mService.getMyReports("active")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportsResponse -> {
                    if (reportsResponse.success) {
                        myOpenReports.onNext(reportsResponse.data);
                    } else
                        Toast.makeText(mContext, reportsResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not get reports", Toast.LENGTH_LONG).show();
                });
        mService.getMyReports("inactive")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportsResponse -> {
                    if (reportsResponse.success) {
                        myClosedResolvedReports.onNext(reportsResponse.data);
                    } else
                        Toast.makeText(mContext, reportsResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not get reports", Toast.LENGTH_LONG).show();
                });

    }

    public void getReport(int id) {
        mService.getReport(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reportResponse -> {
                    if (reportResponse.success)
                        currentReport.onNext(reportResponse.data);
                    else
                        Toast.makeText(mContext, reportResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not get report", Toast.LENGTH_LONG).show();
                });
    }

    public void updateCurrentReport() {
        mService.updateReport(currentReport.getValue().id, currentReport.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseResponse -> {
                    if (baseResponse.body() != null && baseResponse.body().success) {
//                        getMyReports();
//                        Toast.makeText(mContext, "Report updated", Toast.LENGTH_LONG).show();
                    } else if (baseResponse.errorBody() != null) {
                        Toast.makeText(mContext,
                                new Gson().fromJson(baseResponse.errorBody().string(), BaseResponse.class).error.message
                                , Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(mContext, "Could not update report", Toast.LENGTH_LONG).show();
                });
    }

    public void getMe() {
        User u = Utils.getMe(mContext);
        if (u != null) {
            me.onNext(u);
        }
        mService.getMe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meResponse -> {
                    if (meResponse.success) {
                        me.onNext(meResponse.data);
                        Utils.saveMe(meResponse.data, mContext);
                    } else
                        Toast.makeText(mContext, meResponse.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not load user details", Toast.LENGTH_LONG).show();
                });
    }

    public void getTransgressorReport() {
        mService.getTransgressorReport(currentReport.getValue().id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tr -> {
                    if (tr.success) {
                        if (tr.data.size() > 0 && tr.data.get(0).id >= 0)
                            currentTransgressorReport.onNext(tr.data.get(0));
                        else
                            currentTransgressorReport.onNext(new TransgressorReport());
                    } else
                        Toast.makeText(mContext, tr.error.message, Toast.LENGTH_LONG).show();
                }, throwable -> {
                    Toast.makeText(mContext, "Could not load user details", Toast.LENGTH_LONG).show();
                });
    }


    public void addWitnesses() {
        if (mSelectedUsersForWitnessOrAggressorReport.size() > 0) {
            if (me.getValue().retail==1) {
                ArrayList<RetailUser> retailUsers = new ArrayList<>();
                for (User u : mSelectedUsersForWitnessOrAggressorReport) {
                    retailUsers.add(u.convertToRetail());
                }
                mService.addRetailWitness(retailUsers, currentReport.getValue().id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(r -> {
                            mSelectedUsersForWitnessOrAggressorReport.clear();
                            if (r.body() != null && r.body().success) {

                            } else if (r.errorBody() != null) {
                                Toast.makeText(mContext,
                                        new Gson().fromJson(r.errorBody().string(), BaseResponse.class).error.message
                                        , Toast.LENGTH_LONG).show();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, "Could not add witnesses", Toast.LENGTH_LONG).show();
                        });
            } else {
                mService.addWitness(mSelectedUsersForWitnessOrAggressorReport, currentReport.getValue().id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(r -> {
                            mSelectedUsersForWitnessOrAggressorReport.clear();
                            if (r.body() != null && r.body().success) {

                            } else if (r.errorBody() != null) {
                                Toast.makeText(mContext,
                                        new Gson().fromJson(r.errorBody().string(), BaseResponse.class).error.message
                                        , Toast.LENGTH_LONG).show();
                            }
                        }, throwable -> {
                            Toast.makeText(mContext, "Could not add witnesses", Toast.LENGTH_LONG).show();
                        });
            }
        }
    }

    public void resolveCurrentReport() {

    }

    public static class UrlSender {
        public File file;
        public String bucketName;
        public String folderName;
        public String key;
        public int observerId;
        private HarassmentRepository repository;
        private int reportId = 0;
        private boolean isUploadFinished;
        private String url;
        private String type = "";

        public UrlSender(HarassmentRepository repository, File file, String bucketName, String folderName, String key, int id, String url) {
            this.repository = repository;
            this.file = file;
            this.bucketName = bucketName;
            this.folderName = folderName;
            this.key = key;
            this.observerId = id;
            this.url = url;
        }

        public void setReportId(int id) {
            reportId = id;
        }

        public boolean hasReportId() {
            return reportId != 0;
        }

        public void setUploadFinished() {
            isUploadFinished = true;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void trySendingUrl() {
            if (reportId == 0) return;
            if (!isUploadFinished) return;
            if (url == null || url.isEmpty()) return;

            ArrayList<FileUrl> fileUrls = new ArrayList<>();
            fileUrls.add(new FileUrl(url));
            switch (type) {
                case "initial":
                    sendInitialReportFiles(fileUrls);
                    break;
                case "witness":
                    sendWitnessFiles(fileUrls);
                    break;
                case "aggressor":
                    sendAggressorFiles(fileUrls);
                    break;
            }

        }

        private void sendInitialReportFiles(ArrayList<FileUrl> fileUrls) {
            Disposable disposable = repository.mService.sendUploadedFilesUrls(reportId, fileUrls).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> {
                                Log.d("Sender", "url sent to report " + reportId);
                            },
                            e -> e.printStackTrace());
        }

        private void sendWitnessFiles(ArrayList<FileUrl> fileUrls) {
            Disposable disposable = repository.mService.sendWitnessUploadedFilesUrls(reportId, fileUrls).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> {
                                Log.d("Sender", "url sent to witness report " + reportId);
                            },
                            e -> e.printStackTrace());
        }

        private void sendAggressorFiles(ArrayList<FileUrl> fileUrls) {
            Disposable disposable = repository.mService.sendAggressorUploadedFilesUrls(reportId, fileUrls).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(response -> {
                                Log.d("Sender", "url sent to aggressor report " + reportId);
                            },
                            e -> e.printStackTrace());
        }
    }
}
