package kz.pryahin.bitlabInternship.main.exceptions;

public class LessonNotFoundException extends RuntimeException {
  public LessonNotFoundException() {
    super("Lesson not found");
  }
}
