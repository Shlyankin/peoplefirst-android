package rokolabs.com.peoplefirst.api;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import rokolabs.com.peoplefirst.model.ConfirmationRefResponse;
import rokolabs.com.peoplefirst.model.EscalationLevel;
import rokolabs.com.peoplefirst.model.FileUrl;
import rokolabs.com.peoplefirst.model.Report;
import rokolabs.com.peoplefirst.model.RetailActivationRequest;
import rokolabs.com.peoplefirst.model.RetailCompany;
import rokolabs.com.peoplefirst.model.RetailEscalationLevel;
import rokolabs.com.peoplefirst.model.RetailRegistrationRequest;
import rokolabs.com.peoplefirst.model.RetailReport;
import rokolabs.com.peoplefirst.model.RetailUser;
import rokolabs.com.peoplefirst.model.RetailWitnessReport;
import rokolabs.com.peoplefirst.model.RetailWitnessTestimony;
import rokolabs.com.peoplefirst.model.RetailWitnessTestimonyRequest;
import rokolabs.com.peoplefirst.model.TransgressorReport;
import rokolabs.com.peoplefirst.model.User;
import rokolabs.com.peoplefirst.model.VictimTestimony;
import rokolabs.com.peoplefirst.model.VictimTestimonyRequest;
import rokolabs.com.peoplefirst.model.WitnessReport;
import rokolabs.com.peoplefirst.model.WitnessTestimony;
import rokolabs.com.peoplefirst.model.WitnessTestimonyRequest;
import rokolabs.com.peoplefirst.model.requests.LoginRequest;
import rokolabs.com.peoplefirst.model.requests.PushNotificationsTokenRequest;
import rokolabs.com.peoplefirst.model.requests.ResendCodeRequest;
import rokolabs.com.peoplefirst.model.requests.ResetPasswordRequest;
import rokolabs.com.peoplefirst.model.requests.UpdatePasswordRequest;
import rokolabs.com.peoplefirst.model.requests.UserActivastionRequest;
import rokolabs.com.peoplefirst.model.requests.ValidateEmailRequest;
import rokolabs.com.peoplefirst.model.responses.BaseResponse;
import rokolabs.com.peoplefirst.model.responses.CounsellingServicesResponse;
import rokolabs.com.peoplefirst.model.responses.EscalationLevelsResponse;
import rokolabs.com.peoplefirst.model.responses.HarasmentReasonsResponse;
import rokolabs.com.peoplefirst.model.responses.HarasmentTypesResponse;
import rokolabs.com.peoplefirst.model.responses.LoginRespose;
import rokolabs.com.peoplefirst.model.responses.MeResponse;
import rokolabs.com.peoplefirst.model.responses.PushNotificationsTokenResponse;
import rokolabs.com.peoplefirst.model.responses.ReportResponse;
import rokolabs.com.peoplefirst.model.responses.ReportsResponse;
import rokolabs.com.peoplefirst.model.responses.SingleTransgressorReportResponse;
import rokolabs.com.peoplefirst.model.responses.SingleWitnessReportResponse;
import rokolabs.com.peoplefirst.model.responses.TransgressorReportResponse;
import rokolabs.com.peoplefirst.model.responses.UploadedFilesResponse;
import rokolabs.com.peoplefirst.model.responses.UserActivationResponse;
import rokolabs.com.peoplefirst.model.responses.UsersResponse;
import rokolabs.com.peoplefirst.model.responses.WitnessReportResponse;

/**
 * Created by S on 17.05.2018.
 */

public class PeopleFirstService {

    private PeopleFirstApi mApi;

    public PeopleFirstService(PeopleFirstApi mApi) {

        this.mApi = mApi;
    }

    public Single<LoginRespose> auth(String email, String password) {
        return mApi.auth(new LoginRequest(email,password));
    }

    public Single<HarasmentTypesResponse> getHarassmentTypes() {
        return mApi.getHarassmentTypes();
    }

    public Single<HarasmentReasonsResponse> getHarassmentReasons(){ return mApi.getHarassmentReasons(); }

    public Single<MeResponse> getMe() {
        return mApi.getMe();
    }

    public Single<UsersResponse> getUsers(int companyId, String search) {
        return mApi.getUsers(companyId, search);
    }

    public Single<ReportsResponse> getReports() {
        return mApi.getReports();
    }

    public Single<ReportsResponse> getMyReports(String status){
        return mApi.getMyReports(status);
    }

    public Single<ReportResponse> addReport(Report report) {
        return mApi.addReport(report);
    }

    public Single<ReportResponse> addRetailReport(RetailReport report) {
        return mApi.addRetailReport(report);
    }

    public Single<ReportResponse> getReport(int id) {
        return mApi.getReport(id);
    }

    public Single<Response<BaseResponse>> updateReport(int id, Report report) {
        return mApi.updateReport( report);
    }

    public Single<BaseResponse> logout() {
        return mApi.logout();
    }

    public Single<SingleWitnessReportResponse> addWitnessReport(int id, WitnessReport witnessReport) {
        return mApi.addWitnessReport(id, witnessReport);
    }

    public Single<SingleWitnessReportResponse> addRetailWitnessReport(int id, RetailWitnessReport witnessReport) {
        return mApi.addRetailWitnessReport(id, witnessReport);
    }

    public Single<WitnessReportResponse> getWitnessReport(int id) {
        return mApi.getWitnessReport(id);
    }

    public Single<BaseResponse> updateWitnessReport(int id, WitnessReport report) {
        return mApi.updateWitnessReport(id, report);
    }

    public Single<SingleTransgressorReportResponse> addTransgressorReport(int id, TransgressorReport witnessReport) {
        return mApi.addTransgressorReport(id, witnessReport);
    }

    public Single<TransgressorReportResponse> getTransgressorReport(int id) {
        return mApi.getTransgressorReport(id);
    }

    public Single<BaseResponse> updateTransgressorReport(int id, TransgressorReport report) {
        return mApi.updateTransgressorReport(id, report);
    }

    public Single<BaseResponse> resolveReport(int id) {
        return mApi.resolveReport(id);
    }

    public Single<BaseResponse> victimConfirmReport(int id, String action) {
        return mApi.victimConfirmReport(id, action);
    }

    public Single<BaseResponse> closeReport(int id) {
        return mApi.closeReport(id);
    }

    public Single<BaseResponse> reopenReport(int id) {
        return mApi.reopenReport(id);
    }

    public Single<EscalationLevelsResponse> getEscalationLevels() {
        return mApi.getEscalationLevels();
    }

    public Single<CounsellingServicesResponse> getCounsellingServices(int companyId) {
        return mApi.getCounsellingServices(companyId);
    }

    public Single<UserActivationResponse> updateMe(User me) {
        return mApi.updateMe(me);
    }

    public Single<BaseResponse> resetPassword(String email) {
        return mApi.resetPassword(new ResetPasswordRequest(email));
    }

    public Single<UserActivationResponse> userActivation(String email, String password, String first_name, String last_name) {
        return mApi.userActivation(new UserActivastionRequest(email, password,first_name, last_name));
    }

    public Single<UploadedFilesResponse> getReportAttachments(int id) {
        return mApi.getReportAttachments(id);
    }

    public Single<UploadedFilesResponse> sendUploadedFilesUrls(int id, ArrayList<FileUrl> urlsList) {
        return mApi.sendUploadedFilesUrls(id, urlsList);
    }

    public Single<UploadedFilesResponse> sendWitnessUploadedFilesUrls(int id, ArrayList<FileUrl> urlsList) {
        return mApi.sendWitnessUploadedFilesUrls(id, urlsList);
    }

    public Single<UploadedFilesResponse> sendAggressorUploadedFilesUrls(int id, ArrayList<FileUrl> urlsList) {
        return mApi.sendAggressorUploadedFilesUrls(id, urlsList);
    }

    public Single<Response<BaseResponse>> addWitness(@Body List<User> users, int reportId) {
        return mApi.addWitness(users, reportId);
    }

    public Single<Response<BaseResponse>> addRetailWitness(List<RetailUser> users, int reportId) {
        return mApi.addRetailWitness(users, reportId);
    }

	public Single<Response<PushNotificationsTokenResponse>> sendPushNotificationsToken(String deviceId, String token) {
		return mApi.sendPushNotificationsToken(new PushNotificationsTokenRequest(deviceId, token));
	}

    public Single<BaseResponse> deletePushNotificationsToken(String deviceId) {
        return mApi.deletePushNotificationsToken(deviceId);
    }

    public Single<Response<BaseResponse>> rejectWitnessReport(int id) {
        return mApi.rejectWitnessReport(id);
    }

    public Single<Response<BaseResponse>> rejectTransgressorReport(int id) {
        return mApi.rejectTransgressorReport(id);
    }

    public Single<BaseResponse> addVictimTestimony(int id, VictimTestimony testimony){
        return mApi.addVictimTestimony(id, new VictimTestimonyRequest("victim_confirmation", testimony));
    }

    public Single<ConfirmationRefResponse> addWitnessTestimony(int id, WitnessTestimony testimony){
        return mApi.addWitnessTestimony(id, new WitnessTestimonyRequest("witness_confirmation", testimony));
    }

    public Single<ConfirmationRefResponse> addRetailWitnessTestimony(int id, RetailWitnessTestimony testimony) {
        return mApi.addRetailWitnessTestimony(id, new RetailWitnessTestimonyRequest("witness_confirmation",testimony));
    }

    public Single<BaseResponse> rejectTestimony(int id) {
        return mApi.rejectTestimony(id);
    }

    public Single<Response<BaseResponse>> retailRegistration(RetailRegistrationRequest request) {
        return mApi.retailRegistration(request);
    }


    public Single<Response<LoginRespose>> retailActivation(RetailActivationRequest request) {
        return mApi.retailActivation(request);
    }

    public Single<ArrayList<RetailCompany>> getCompanies(String query) {
        return mApi.getCompanies(query).map(retailCompaniesResponseResponse -> retailCompaniesResponseResponse.data);
    }

    public Single<BaseResponse> resendCode(String email) {
        return mApi.resendCode(new ResendCodeRequest(email));
    }

    public Single<BaseResponse> addEscalationLevels(List<EscalationLevel> levels) {
        return mApi.addEscalationLevels(levels);
    }
    public Single<BaseResponse> validateEmail(ValidateEmailRequest request) {
        return mApi.validateEmail(request);
    }
    public Single<BaseResponse> setNewPassword(UpdatePasswordRequest request) {
        return mApi.updatePassword(request);
    }

}
