package rokolabs.com.peoplefirst.api;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rokolabs.com.peoplefirst.model.ConfirmationRefResponse;
import rokolabs.com.peoplefirst.model.EscalationLevel;
import rokolabs.com.peoplefirst.model.FileUrl;
import rokolabs.com.peoplefirst.model.Report;
import rokolabs.com.peoplefirst.model.RetailActivationRequest;
import rokolabs.com.peoplefirst.model.RetailEscalationLevel;
import rokolabs.com.peoplefirst.model.RetailRegistrationRequest;
import rokolabs.com.peoplefirst.model.RetailReport;
import rokolabs.com.peoplefirst.model.RetailUser;
import rokolabs.com.peoplefirst.model.RetailWitnessReport;
import rokolabs.com.peoplefirst.model.RetailWitnessTestimonyRequest;
import rokolabs.com.peoplefirst.model.TransgressorReport;
import rokolabs.com.peoplefirst.model.User;
import rokolabs.com.peoplefirst.model.VictimTestimonyRequest;
import rokolabs.com.peoplefirst.model.WitnessReport;
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
import rokolabs.com.peoplefirst.model.responses.RetailCompaniesResponse;
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

public interface PeopleFirstApi {
//old
//    @FormUrlEncoded
//    @POST("auth/login")
//    Single<LoginRespose> auth(@Field("email") String email, @Field("password") String password);
    @POST("auth/login")
    Single<LoginRespose> auth(@Body LoginRequest loginRequest);

    @GET("harassment-types")
    Single<HarasmentTypesResponse> getHarassmentTypes();

    @GET("harassment-reasons")
    Single<HarasmentReasonsResponse> getHarassmentReasons();

    @GET("users/me")
    Single<MeResponse> getMe();

    @GET("users?per-page=100&page=1")
    Single<UsersResponse> getUsers(@Query("company-id") int companyId, @Query("search") String search);

    @GET("myOpenReports")
    Single<ReportsResponse> getReports();

    @GET("reports/my/{status}?per-page=100&page=1")
    Single<ReportsResponse> getMyReports(@Path("status") String status);

    @POST("reports")
    Single<ReportResponse> addReport(@Body Report report);

    @POST("reports")
    Single<ReportResponse> addRetailReport(@Body RetailReport report);


    @GET("reports/{id}")
    Single<ReportResponse> getReport(@Path("id") int id);

    @HTTP(method = "PATCH", path = "reports", hasBody = true)
    Single<Response<BaseResponse>> updateReport( @Body Report report);

    @POST("auth/logout")
    Single<BaseResponse> logout();

    @POST("/witness-reports/{id}")
    Single<SingleWitnessReportResponse> addWitnessReport(@Path("id") int id, @Body WitnessReport witnessReport);

    @POST("/witness-reports/{id}")
    Single<SingleWitnessReportResponse> addRetailWitnessReport(@Path("id") int id, @Body RetailWitnessReport witnessReport);

    @GET("/witness-reports/{id}")
    Single<WitnessReportResponse> getWitnessReport(@Path("id") int id);

    @HTTP(method = "PATCH", path = "/witness-reports/{id}", hasBody = true)
    Single<BaseResponse> updateWitnessReport(@Path("id") int id, @Body WitnessReport report);

    @POST("/transgressor-reports/{id}")
    Single<SingleTransgressorReportResponse> addTransgressorReport(@Path("id") int id, @Body TransgressorReport witnessReport);

    @GET("/transgressor-reports/{id}")
    Single<TransgressorReportResponse> getTransgressorReport(@Path("id") int id);

    @HTTP(method = "PATCH", path = "/transgressor-reports/{id}", hasBody = true)
    Single<BaseResponse> updateTransgressorReport(@Path("id") int id, @Body TransgressorReport report);


    @POST("/reports/{id}/resolve")
    Single<BaseResponse> resolveReport(@Path("id") int id);

    @POST("/reports/{id}/confirmation/{action}")
    Single<BaseResponse> victimConfirmReport(@Path("id") int id, @Path("action") String action);


    @POST("/reports/{id}/close")
    Single<BaseResponse> closeReport(@Path("id") int id);

    @POST("/reports/{id}/reopen")
    Single<BaseResponse> reopenReport(@Path("id") int id);

    @GET("/reports/{id}/files")
    Single<UploadedFilesResponse> getReportAttachments(@Path("id") int id);

    @POST("/reports/{id}/files")
    Single<UploadedFilesResponse> sendUploadedFilesUrls(@Path("id") int id, @Body ArrayList<FileUrl> body);

    @POST("/witness-reports/{id}/files")
    Single<UploadedFilesResponse> sendWitnessUploadedFilesUrls(@Path("id") int id, @Body ArrayList<FileUrl> body);

    @POST("/transgressor-reports/{id}/files")
    Single<UploadedFilesResponse> sendAggressorUploadedFilesUrls(@Path("id") int id, @Body ArrayList<FileUrl> body);

    @GET("/companies/my/escalation-levels")
    Single<EscalationLevelsResponse> getEscalationLevels();

    @GET("/companies/{id}/counselling-services")
    Single<CounsellingServicesResponse> getCounsellingServices(@Path("id") int companyId);

    @HTTP(method = "PATCH", path = "/users/me", hasBody = true)
    Single<UserActivationResponse> updateMe(@Body User me);

    @POST("/auth/reset-password")
    Single<BaseResponse> resetPassword(@Body ResetPasswordRequest request);
    @POST("/auth/update-password")
    Single<BaseResponse> updatePassword(@Body UpdatePasswordRequest request);
    @POST("/users/activation")
    Single<UserActivationResponse> userActivation(@Body UserActivastionRequest request);

    @POST("/reports/{id}/add-witnesses")
    Single<Response<BaseResponse>> addWitness(@Body List<User> users, @Path("id") int reportId);

    @POST("/reports/{id}/add-witnesses")
    Single<Response<BaseResponse>> addRetailWitness(@Body List<RetailUser> users, @Path("id") int reportId);

	@POST("/push-notifications/tokens")
	Single<Response<PushNotificationsTokenResponse>> sendPushNotificationsToken(@Body PushNotificationsTokenRequest request);

    @DELETE("/push-notifications/tokens/{id}")
    Single<BaseResponse> deletePushNotificationsToken(@Path("id") String deviceId);

    @POST("/witness-reports/{report_id}/reject")
    Single<Response<BaseResponse>> rejectWitnessReport(@Path("report_id") int id);

    @POST("/transgressor-reports/{report_id}/reject")
    Single<Response<BaseResponse>> rejectTransgressorReport(@Path("report_id") int id);

    @GET("/reports/{report_id}/testimonies")
    Single<Object> getTestimonies(@Path("report_id") int id);

    @POST("/reports/{report_id}/testimonies")
    Single<BaseResponse> addVictimTestimony(@Path("report_id") int id, @Body VictimTestimonyRequest testimonyRequest);

    @POST("/reports/{report_id}/testimonies")
    Single<ConfirmationRefResponse> addWitnessTestimony(@Path("report_id") int id, @Body WitnessTestimonyRequest testimonyRequest);

    @POST("/reports/{report_id}/testimonies")
    Single<ConfirmationRefResponse> addRetailWitnessTestimony(@Path("report_id") int id, @Body RetailWitnessTestimonyRequest testimonyRequest);

    @POST("/reports/{report_id}/testimonies/reject")
    Single<BaseResponse> rejectTestimony(@Path("report_id") int id);
//old
//    @POST("/users/retail-registration")
//    Single<Response<BaseResponse>> retailRegistration(@Body RetailRegistrationRequestOld request);

    @POST("/users/registration")
    Single<Response<BaseResponse>> retailRegistration(@Body RetailRegistrationRequest request);

    @POST("/users/retail-activation")
    Single<Response<LoginRespose>> retailActivation(@Body RetailActivationRequest request);

    @GET("/retail/companies")
    Single<RetailCompaniesResponse> getCompanies(@Query("q") String query);

    @POST("/users/retail-resend-activation-code")
    Single<BaseResponse> resendCode(@Body ResendCodeRequest request);

    @POST("/companies/my/escalation-levels")
    Single<BaseResponse> addEscalationLevels(@Body List<EscalationLevel> levels);

    @POST("/users/validate-email")
    Single<BaseResponse> validateEmail(@Body ValidateEmailRequest request);
}
