package com.ossi.genreporting.api;


import com.ossi.genreporting.model.AbsentStatusResponse;
import com.ossi.genreporting.model.AdjustResponse;
import com.ossi.genreporting.model.AllEventsShow;
import com.ossi.genreporting.model.AllProductResponseItem;
import com.ossi.genreporting.model.AllRequestResponse;
import com.ossi.genreporting.model.ApplyExpenseResponseItem;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;
import com.ossi.genreporting.model.ApplyWFHResponseItem;
import com.ossi.genreporting.model.AttendanceStatusResponse;
import com.ossi.genreporting.model.CreateProductModelResponse;
import com.ossi.genreporting.model.DepartmentEmployeeListResponse;
import com.ossi.genreporting.model.DetailsResponseItem;
import com.ossi.genreporting.model.EmployeeListResponse;
import com.ossi.genreporting.model.EventResponse;
import com.ossi.genreporting.model.ExpenseViewResponse;
import com.ossi.genreporting.model.HistoryRequestResponse;
import com.ossi.genreporting.model.HolidayListResponse;
import com.ossi.genreporting.model.LeaveStatusREsponse;
import com.ossi.genreporting.model.LoginResponse;
import com.ossi.genreporting.model.MyProjectListResponse;
import com.ossi.genreporting.model.MyTaskListResponse;
import com.ossi.genreporting.model.PayrollAttendanceShowResponse;
import com.ossi.genreporting.model.PayrollSalaryHistoryResponse;
import com.ossi.genreporting.model.PayrollSalaryShowResponse;
import com.ossi.genreporting.model.PresentStatusItem;
import com.ossi.genreporting.model.ProfileDetailsResponseItem;
import com.ossi.genreporting.model.ShowMeetingListResponse;
import com.ossi.genreporting.model.UpdateResponse;
import com.ossi.genreporting.model.ViewAtendanceResponseItemItem;
import com.ossi.genreporting.model.ViewLeaveStatus;
import com.ossi.genreporting.model.ViewWorkFromHomeResponse;
import com.ossi.genreporting.model.WFHStatusResponse;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("login")
    Call<List<LoginResponse>> get_Login_report(@Query("Username") String user_name,
                                               @Query("Password") String pass_word,
                                               @Query("Lat") String lat,
                                               @Query("Lon") String lon);

    @GET("Logout")
    Call<List<LoginResponse>> get_Logout_report(@Query("UserID") String UserID,
                                                @Query("LoginID") String LoginID);

    @GET("ForgetPassword")
    Call<List<LoginResponse>> get_forgot_password(@Query("EmpCode") String user_name,
                                                  @Query("Password") String pass_word);

    @GET("ShowTotalCount")
    Call<List<DetailsResponseItem>> get_all_detail(@Query("EmpCode") String user_name);

    @FormUrlEncoded
    @POST("ApplyExpenses")
    Call<List<ApplyExpenseResponseItem>> ApplyExpenses_submit(@Field("EmpCode") String EmpCode,
                                                              @Field("Amount") String Amount,
                                                              @Field("Purpose") String Purpose,
                                                              @Field("Image") String Image);

    @GET("GetExpense")
    Call<List<ExpenseViewResponse>> Get_Expenses_submit(@Query("UserId") String EmpCode,
                                                        @Query("Expense_Type") String Purpose);

    @GET("GetWorkFromHome")
    Call<List<ViewWorkFromHomeResponse>> get_view_work_from_home(@Query("UserId") String EmpCode,
                                                                 @Query("Month") String Month,
                                                                 @Query("Type") String Type);


    @GET("GetLeave")
    Call<List<ViewLeaveStatus>> get_view_leave(@Query("UserId") String EmpCode,
                                               @Query("Month") String Month,
                                               @Query("ApprovedType") String ApprovedType,
                                               @Query("LeaveType") String LeaveType);

    @GET("ApplyLeave")
    Call<List<ApplyLeaveResponseItem>> ApplyLeave_submit(@Query("EmpCode") String EmpCode,
                                                         @Query("FDate") String FDate,
                                                         @Query("TDate") String TDate,
                                                         @Query("TypeLeave") String TypeLeave,
                                                         @Query("Purpose") String Purpose,
                                                         @Query("LeaveTime")String LeaveTime);


    @GET("ApplyWFH")
    Call<List<ApplyWFHResponseItem>> ApplyWFH_submit(@Query("EmpCode") String EmpCode,
                                                     @Query("FDate") String FDate,
                                                     @Query("TDate") String TDate,
                                                     @Query("Purpose") String Purpose);

    @GET("ViewAttendance")
    Call<List<ViewAtendanceResponseItemItem>> Apply_view_attendance(@Query("EmpCode") String EmpCode,
                                                                    @Query("FDate") String FDate,
                                                                    @Query("TDate") String TDate,
                                                                    @Query("TypeAttendance") String TypeAttendance);


    @GET("UpdateProfileInfo")
    Call<List<UpdateResponse>> Update_profile(@Query("EmpCode") String EmpCode,
                                              @Query("Mobile") String Mobile,
                                              @Query("MaritailStatus") String MaritailStatus,
                                              @Query("Password") String Password);

    @FormUrlEncoded
    @POST("UpdateImage")
    Call<List<UpdateResponse>> Update_profile_img(@Field("EmpCode") String EmpCode,
                                                  @Field("Image") String Image);

    @GET("ViewProfile")
    Call<List<ProfileDetailsResponseItem>> get_all_profile_details(@Query("EmpCode") String user_name);


    @GET("Count_LoginOrHour")
    Call<List<PresentStatusItem>> get_previous_week_hour_list(@Query("User_id") String User_id);


    @GET("GetEvent")
    Call<List<EventResponse>> get_event_details();

    @GET("EmployeeNameList")
    Call<List<EmployeeListResponse>> get_employee_list();


    @GET("GetHoliday")
    Call<List<HolidayListResponse>> get_holiday_list();

    @FormUrlEncoded
    @POST("Showprojectlist")
    Call<List<MyProjectListResponse>> get_assign_project_lists(@Field("user_id") String user_id);


    @GET("AssingeWorking")
    Call<List<ApplyLeaveResponseItem>> Assigned_task_by_me(@Query("Assign_to_Sno") String Assign_to_Sno,
                                                           @Query("Project_id") String Project_id,
                                                           @Query("Task_Module") String Task_Module,
                                                           @Query("Task_Deadline") String Task_Deadline,
                                                           @Query("Task_Detail") String Task_Detail,
                                                           @Query("Assign_By_Sno") String Assign_By_Sno);

    @GET("Applyasset")
    Call<List<ApplyLeaveResponseItem>> employee_request_send(@Query("User_id") String User_id,
                                                             @Query("asset_Name") String asset_Name,
                                                             @Query("Quantity") String Quantity,
                                                             @Query("Purpose") String Purpose);

    @GET("SubmitTask")
    Call<List<ApplyLeaveResponseItem>> submit_task(@Query("id") String id,
                                                   @Query("Status") String Status,
                                                   @Query("Remark") String Remark,
                                                   @Query("Project_id") String project_id,
                                                   @Query("Task_id") String task_id);

    @GET("request_approved_or_reject")
    Call<List<ApplyLeaveResponseItem>> req_submit(@Query("id") String id,
                                                  @Query("User_id") String User_id,
                                                  @Query("Type") String Type,
                                                  @Query("Status") String Status);


    @GET("Currentprojecttask")
    Call<List<MyTaskListResponse>> get_my_task_lists(@Query("user_id") String user_id,
                                                     @Query("Project_id") String Project_id);

    @GET("Currentproject")
    Call<List<MyProjectListResponse>> get_my_project_lists(@Query("user_id") String sno);

    @GET("ShowMeetto")
    Call<List<ShowMeetingListResponse>> get_meting_lists(@Query("user_id") String sno);

    @GET("ShowMeetby")
    Call<List<ShowMeetingListResponse>> get_current_meting_lists(@Query("user_id") String sno);

    @GET("AssignTaskList")
    Call<List<MyProjectListResponse>> get_assign_task_lists(@Query("Sno") String sno);

    @GET("AttendanceStatus")
    Call<List<AttendanceStatusResponse>> get_attendance_status();

    @GET("ShowAllAbsentEmployeeList")
    Call<List<AbsentStatusResponse>> get_absent_list(@Query("Type") String Type);

    @GET("ShowAllLeaveEmployeeList")
    Call<List<LeaveStatusREsponse>> get_leave_list(@Query("Type") String Type);

    @GET("ShowAllPresentEmployeeList")
    Call<List<PresentStatusItem>> get_present_list(@Query("Type") String Type);

    @GET("ShowAllWFHEmployeeList")
    Call<List<WFHStatusResponse>> get_wfh_list();

    @GET("All_Request_List")
    Call<List<AllRequestResponse>> get_all_req_lists(@Query("sno") String sno,
                                                     @Query("Requesst_Type") String Requesst_Type);

    @GET("ApprovedRequestHistory")
    Call<List<HistoryRequestResponse>> get_History_approved_request(@Query("user_id") String user_id,
                                                                    @Query("request_type") String request_type,
                                                                    @Query("FromDate") String FromDate,
                                                                    @Query("ToDate") String ToDate,
                                                                    @Query("status_type") String status_type);

    @GET("add_Event")
    Call<List<AllEventsShow>> submit_event(@Query("user_id") String user_id,
                                                    @Query("Heading") String Heading,
                                                    @Query("EventDate") String EventDate,
                                                    @Query("Description") String Description,
                                                    @Query("Venue") String Venue,
                                                    @Query("Organiser") String Organiser,
                                                    @Query("Location") String Location);


    @GET("view_Event_list")
    Call<List<AllEventsShow>> get_all_events_lists(@Query("User_id") String User_id);

    @GET("delete_Event")
    Call<List<AllEventsShow>> delete_events_lists(@Query("id") String event_id);

    @GET("edit_Event")
    Call<List<AllEventsShow>> edit_events_lists(@Query("id") String event_id,
                                                @Query("Heading") String Heading,
                                                @Query("EventDate") String EventDate,
                                                @Query("Description") String Description,
                                                @Query("Venue") String Venue,
                                                @Query("Organiser") String Organiser,
                                                @Query("Location") String Location);

    @GET("DepartementEmpolyee")
    Call<List<DepartmentEmployeeListResponse>> employee_list_department_wise(@Query("Department") String Heading);

    @GET("view_Department_list")
    Call<List<DepartmentEmployeeListResponse>> department_list();

    @GET("add_Meeting")
    Call<List<ShowMeetingListResponse>> add_meeting(@Query("user_id") String user_id,
                                                   @Query("MeetingHeading") String MeetingHeading,
                                                   @Query("MeetingDate") String MeetingDate,
                                                   @Query("MeetingTime") String MeetingTime,
                                                   @Query("Department") String Department,
                                                   @Query("MetType") String MetType,
                                                   @Query("employees") String employees,
                                                   @Query("meetingDetails") String meetingDetails,
                                                   @Query("description") String description);

    @GET("Edit_Meeting")
    Call<List<ShowMeetingListResponse>> edit_meeting(@Query("user_id") String user_id,
                                                     @Query("MeetingId") String meeting_id,
                                                    @Query("MeetingHeading") String MeetingHeading,
                                                    @Query("MeetingDate") String MeetingDate,
                                                    @Query("MeetingTime") String MeetingTime,
                                                    @Query("Department") String Department,
                                                    @Query("MetType") String MetType,
                                                    @Query("employees") String employees,
                                                    @Query("meetingDetails") String meetingDetails,
                                                    @Query("description") String description);

    //delete_meeting
    @GET("Delete_MeetingData")
    Call<List<ShowMeetingListResponse>> delete_meeting(@Query("user_id") String user_id,
                                                    @Query("MeetingId") String meeting_id);


    @GET("AttendanceApprovalShow")
    Call<List<PayrollAttendanceShowResponse>> req_attendance_approval(@Query("UserId") String UserId);
//SalaryApprovalshow

    @GET("SalaryApprovalshow")
    Call<List<PayrollSalaryShowResponse>> req_salary_approval(@Query("UserId") String UserId);

    @GET("AdjustDay")
    Call<List<AdjustResponse>> adjust_day_edit(@Query("userId") String userId,
                                               @Query("uniqueId") String uniqueId,
                                               @Query("adjustDay") String adjustDay);

    @GET("AttendanceApproved")
    Call<List<PayrollAttendanceShowResponse>> approved_attendance(@Query("userId") String userId,
                                                                  @Query("status") String status);

    @GET("SalaryApproved")
    Call<List<PayrollSalaryShowResponse>> approved_salary(@Query("userId") String userId,
                                                          @Query("status") String status);

    @GET("SalaryHistory")
    Call<List<PayrollSalaryHistoryResponse>> history_salary(@Query("year") String year,
                                                            @Query("Month") String Month,
                                                            @Query("EmailType") String EmailType,
                                                            @Query("Sno") String Sno);


    @GET("LoginOrHour")
    Call<List<LoginResponse>> get_hour_calculate(@Query("User_id") String user_id);

    //For Product Planner

    // @FormUrlEncoded
    @Multipart
    @POST("api/addproduct")
    Call<CreateProductModelResponse> create_product(@PartMap Map<String, RequestBody> map);

    @GET("api/GetAllProduct")
    Call<List<AllProductResponseItem>> get_all_products(@Query("emp_id") String user_id);

    @GET("RequestCancelAfterApproved")
    Call<List<AdjustResponse>> cancel_req_after_approve(@Query("userId") String userId,
                                               @Query("id") String id,
                                               @Query("days") String days,
                                                        @Query("type") String type);

   /* @FormUrlEncoded
    @POST("api/addproduct")
    Call<CreateProductModelResponse> create_product(@Field("Sno") String Sno,
                                                          @Field("product_name") String product_name,
                                                          @Field("product_description") String product_description,
                                                    @Field("employee_id") String employee_id,
                                                    @Field("assign") String assign,
                                                    @Field("view") String view,
                                                    @Field("member_active") String member_active,
                                                    @Field("progress") String progress,
                                                    @Field("link") String link,
                                                    @Field("version_cnt") String version_cnt,
                                                    @Field("version_name") String version_name,
                                                    @Field("start_date") String start_date,
                                                    @Field("due_date") String due_date,
                                                    @Field("release_date") String release_date,
                                                    @Field("attachement") String attachement,
                                                    @Field("title") String title,
                                                    @Field("file_url") String file_url);*/
}


