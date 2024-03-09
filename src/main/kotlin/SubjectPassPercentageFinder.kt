import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import entity.Result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*

suspend fun main() {
    val formatter = SubjectPassPercentageFinder()
    formatter.format()
}
class SubjectPassPercentageFinder {
    private val resultList = mutableListOf<Result>()


    suspend fun format() {
        println("Enter the Result CSV Path")
        val scanner = Scanner(System.`in`)
        var path = scanner.next()
        if (path.startsWith("\"") && path.endsWith("\"")) {
            path = path.trim {
                it == '"'
            }
        }
        try {


            coroutineScope {
                launch {
                    readCsv(path.trim())
                }.invokeOnCompletion {
                    breakLine()
                    println("Enter the Subject Code")
                    val subjectCode = scanner.next()

                    findPercentagePerSubject(subjectCode = subjectCode.trim())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun findPercentagePerSubject(subjectCode: String) {
        var totalPassCount = 0
        val perSubjectList = resultList.filter {
            it.subjectCode == subjectCode
        }
        perSubjectList.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassCount++;
            }
        }
        val percent = (totalPassCount.toFloat() / perSubjectList.size.toFloat()) * 100f
        println("Total Pass Percentage  is : $percent")
    }

    private fun readCsv(path: String) {
        csvReader().open(path) {
            readAllAsSequence().forEachIndexed { ind: Int, row: List<String> ->
                if (ind != 0) {
                    resultList.add(
                        Result(
                            sem = "",
                            date = "",
                            rollNo = row[0],
                            subjectCode = row[1],
                            subject = "",
                            grade = row[4],
                            credits = row[5],
                            internalMarks = ""

                        )
                    )
                }
            }
        }
    }



private fun breakLine() {
    println("--------------------------------------------------------------------")
}
}
