package kz.pryahin.bitlabInternship.exceptions;

public class LessonNotFoundException extends RuntimeException {
	public LessonNotFoundException() {
		super("Lesson not found");
	}
}
