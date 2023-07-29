package prajna.app.repository.api

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {


    /*Profile Data*/
    @POST("/api/Auth/UserProfile")
    fun getProfile(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Auth/Login")
    fun getLogin(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/FeeDetails/GetStudentFeeDetailsForCollection")
    fun getFeeDetails(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/FeeDetails/SavePaymentResponse")
    fun getSavePaymentResponse(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/GetStudentAttendanceByYearCode")
    fun getAttendance(@Body jsonObject: JsonObject): Call<ResponseBody>


    @POST("/api/Student/GetStudentAssignmrntByStudPKey")
    fun getAssignment(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/FeeDetails/SaveStudentFeePreTransaction")
    fun saveStudentFeePre(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/GetDatewiseStudentAttendacePercentage")
    fun getFeeListDateWise(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/FeeDetails/SaveStudentFeeCollection")
    fun saveStudentFeeCollection(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/SaveQuery")
    fun saveQUery(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/SaveFeedback")
    fun saveFeedback(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/GetStudentProgressCard")
    fun getPrgressCard(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/FeeDetails/GetStudentFeeCollectionDetails")
    fun getfeeHistory(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/GetStudentActivityEvents")
    fun getEvents(@Body string: String): Call<ResponseBody>

    @POST("/api/Student/InsertDeviceTokens")
    fun getNotificationFirebase(@Body jsonObject: JsonObject): Call<ResponseBody>

      @POST("/api/Student/GetAppAlerts")
    fun getNotificationList(@Body jsonObject: JsonObject): Call<ResponseBody>

    @POST("/api/Student/GetGallery")
    fun getGallery(): Call<ResponseBody>




}
