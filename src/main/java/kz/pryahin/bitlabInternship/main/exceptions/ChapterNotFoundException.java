package kz.pryahin.bitlabInternship.main.exceptions;

public class ChapterNotFoundException extends RuntimeException {
  public ChapterNotFoundException() {
    super("Chapter not found");
  }


  public ChapterNotFoundException(String message) {
    super(message);
  }
}
