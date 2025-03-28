package Models;

public class Course {
    private final int course_id;
    private final String course_name;
    private final String course_code;

    public Course(int courseId, String courseName, String courseCode) {
        course_id = courseId;
        course_name = courseName;
        course_code = courseCode;
    }

    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_code() {
        return course_code;
    }

}
