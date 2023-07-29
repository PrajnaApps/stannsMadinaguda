package prajna.app.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import prajna.app.R

class Sample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feehistory_item)
        val json = """[
    {
        "ExamName": "UNIT TEST I 2023",
        "SubjectName": "Telugu",
        "Marks": "98        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    },
    {
        "ExamName": "UNIT TEST I 2023",
        "SubjectName": "English",
        "Marks": "55        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    },
    {
        "ExamName": "UNIT TEST I 2023",
        "SubjectName": "Maths",
        "Marks": "30        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    },
    {
        "ExamName": "UNIT TEST 2",
        "SubjectName": "Telugu",
        "Marks": "88        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    },
    {
        "ExamName": "UNIT TEST 2",
        "SubjectName": "English",
        "Marks": "78        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    },
    {
        "ExamName": "UNIT TEST 2",
        "SubjectName": "Maths",
        "Marks": "98        ",
        "TotalMarks": "100",
        "Passmarks": "35"
    }
]"""
        var jsonArray: JSONArray? = null
        jsonArray = try {
            JSONArray(json)
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
        val groupedJson: MutableMap<String?, MutableList<JSONObject?>> = HashMap()
        for (i in 0 until jsonArray!!.length()) {
            var obj: JSONObject? = null
            var examName: String? = null
            try {
                obj = jsonArray.getJSONObject(i)
                examName = obj.getString("ExamName")
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
            if (groupedJson.containsKey(examName)) {
                groupedJson[examName]!!.add(obj)
            } else {
                val list: MutableList<JSONObject?> = ArrayList()
                list.add(obj)
                groupedJson[examName] = list
            }
        }
        val resultArray = JSONArray()
        for (examName in groupedJson.keys) {
            val groupObj = JSONObject()
            try {
                groupObj.put("ExamName", examName)
                groupObj.put("Subjects", groupedJson[examName])
                resultArray.put(groupObj)
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
        }
        val groupedJsonString = resultArray.toString()
        println(groupedJsonString)
        Log.e("TAG", "onCreate: $groupedJsonString")
    }
}