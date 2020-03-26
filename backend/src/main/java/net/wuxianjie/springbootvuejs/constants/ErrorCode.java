package net.wuxianjie.springbootvuejs.constants;

public enum ErrorCode {

  SUCCESS(0, "成功"),
  USER_NAME_NOT_FOUND(1000, "用户名为空"),
  USER_PASSWORD_NOT_FOUND(1001, "用户密码为空"),
  USER_NAME_ERROR(1002, "用户名错误"),
  USER_PASSWORD_ERROR(1003, "用户密码错误"),
  USER_NAME_OR_PASSWORD_ERROR(1004, "用户名或密码错误");

  private final int value;
  private final String reasonPhrase;

  ErrorCode(int value, String reasonPhrase) {
    this.value = value;
    this.reasonPhrase = reasonPhrase;
  }

  public int getValue() {
    return value;
  }

  public String getReasonPhrase() {
    return reasonPhrase;
  }

  @Override
  public String toString() {
    return "ErrorCode{" +
        "value=" + value +
        ", reasonPhrase='" + reasonPhrase + '\'' +
        '}';
  }
}
