package com.tecsup.coursemaster.data
//
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourseViewModel(
    private val repository: CourseRepository = CourseRepository()
) : ViewModel() {

    private val _ui = MutableStateFlow(emptyList<Course>())
    val ui = _ui.asStateFlow()

    fun load(userId: String) {
        viewModelScope.launch {
            repository.getCourses(userId).collect {
                _ui.value = it
            }
        }
    }

    fun addCourse(course: Course) {
        viewModelScope.launch { repository.add(course) }
    }

    fun updateCourse(course: Course) {
        viewModelScope.launch { repository.update(course) }
    }

    fun deleteCourse(id: String) {
        viewModelScope.launch { repository.delete(id) }
    }
}
