
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import entity.Result
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*


// This will find the pass percentage by total number of passed Subjects by total Number of subjects written
// eg : 5 students with each having 10 subjects , each of them had passed 6 subjects then it will be 30/50

suspend fun main() {
    val percentageFinder = OverallPassPercentageFinder()
    percentageFinder.formatter()
}

//
class OverallPassPercentageFinder {
    private val resultList = mutableListOf<Result>()


    suspend fun formatter() {

        println("Enter The File Path Which contains only results")
        val scanner = Scanner(System.`in`)
        var path = scanner.next()
        val inverted = 34 // ascii value of " is 34
        if (path.startsWith("\"")&&path.endsWith("\"")){
          path=  path.trim {
                it == inverted.toChar()
            }
        }
        try {


        coroutineScope {
            /**
             * Paths should be changed everytime for specific result percentage.
             */

            launch {
                readCsv(path.trim())
            }.invokeOnCompletion {
                findTotalPercentage()
                breakLine()
                findOnlyWrittenSubsPercent()
                breakLine()

            }
        }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun findPercentagePerSubject(subjectCode: String, subjectName: String) {
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
        println("Total Pass Percentage of $subjectName is : $percent")
    }

    private fun findTotalPercentage() {
        var totalPassedSubjects = 0

        resultList.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassedSubjects++
            }
        }
        val percent = (totalPassedSubjects.toFloat() / resultList.size.toFloat()) * 100f
        println("Total Pass Percentage : $percent")
    }

    private fun findOnlyWrittenSubsPercent() {
        var totalPassedSubjects = 0
        val onlySubjects = resultList
            .filter {
                (it.credits == "3") || (it.grade == "F") || (it.grade == "ABSENT")
                        && (labs.contains(it.subjectCode).not())
            }
        onlySubjects.forEach {
            if (it.grade != "F" && it.grade != "ABSENT" && it.grade != "AB") {
                totalPassedSubjects++
            }
        }
        val percent = (totalPassedSubjects.toFloat() / onlySubjects.size.toFloat()) * 100f
        println("Total Pass Percentage of written subjects is: $percent")
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