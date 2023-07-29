package prajna.app.repository.model

import com.google.gson.annotations.SerializedName
import com.sun.org.apache.xerces.internal.impl.xpath.XPath.Step


data class InfoDetails(
    val ClassCode: Int,

    val AssignmentDescription: String,
    val AssignmentDate: String,
    val SectionCode: Int,
    val SubjectCode: Int,
    val SchoolId: Int,
    val BranchId: Int,
    val YearCode: Int,
    val IsActive: Boolean,
    val FileName: String,
    val Path: String
)

data class StudentInfo(
    var isSelected: Boolean,
    val StudentName: String,
    val DateOfBirth: String,
    val AadharNo: Int,
    val FatherName: String,
    val StudentGender: String,
    val AdmissionNo: String,
    val ClassCode: Int,
    val ClassName: String,
    val YearCode: Int,
    val StudentPKey: Int
)

data class NotificationInfo(
    var AlertMessage: String
)

data class ProgressreportInfo(
    var SubjectName: String,
    val Marks: String
)

data class Progressreport(
    var ExamName: String,
    var Subjects: String)

data class StudentFeeInfo(
    val FeeTypePkey: Int,
    val FeeTypeDescription: String,
    val PaymentModePKey: Int,
    val Amount: Float,
    val Term: Int,
    val StudentFkey: Int,
    val PaidAmount: Float,
    val Due: Float,
    val CreatedDate: String
)

data class StudentFeeHistoryInfo(
    val PaidAmount: Int,
    val PaymentMode: String,
    val ReceiptNo: Int,
    val TransactionDate: String
)


data class EventsInfo(
    val EventName: String,
    val StartDate: String,
    val EndDate: String,
    val OrganiserName: String
)
data class GalleryModel(@SerializedName("ImagePath"    ) var ImagePath    : String?    = null,
                        @SerializedName("ImageName"    ) var ImageName    : String?    = null)
