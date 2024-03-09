package entity

import kotlinx.serialization.Serializable

/**
 * @property Result - Wraps the result of each subject
 * @param sem : Semester
 * @param date : Date of the exam
 * @param rollNo : Roll no of the student
 * @param subjectCode : Subject code
 * @param subject : Name of the subject
 * @param credits : Total Credits per subject
 * @param internalMarks : Total Internal Marks per subject
 */

@Serializable
data class Result(
    val sem: String,
    val date: String,
    val rollNo: String,
    val subjectCode: String,
    val subject: String,
    val grade: String,
    val credits: String,
    val internalMarks: String,
)