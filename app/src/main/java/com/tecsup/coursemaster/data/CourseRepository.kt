package com.tecsup.coursemaster.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CourseRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private val collection = db.collection("courses")

    fun getCourses(userId: String): Flow<List<Course>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                val list = snapshot?.documents?.map { doc ->
                    Course(
                        id = doc.id,
                        name = doc.getString("name") ?: "",
                        category = doc.getString("category") ?: "",
                        duration = doc.getLong("duration")?.toInt() ?: 0,
                        status = doc.getString("status") ?: "",
                        userId = doc.getString("userId") ?: ""
                    )
                } ?: emptyList()

                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    suspend fun add(course: Course) {
        collection.add(
            mapOf(
                "name" to course.name,
                "category" to course.category,
                "duration" to course.duration,
                "status" to course.status,
                "userId" to course.userId
            )
        )
    }

    suspend fun update(course: Course) {
        collection.document(course.id).update(
            mapOf(
                "name" to course.name,
                "category" to course.category,
                "duration" to course.duration,
                "status" to course.status
            )
        )
    }

    suspend fun delete(id: String) {
        collection.document(id).delete()
    }
}
