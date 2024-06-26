package cn.structure.starter.web.restful.enums;


/**
 * <p>
 * 公共异常枚举
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 20:54
 */
public enum GlobalExceptionEnum {

    SIMPLE("simple"),
    FATHER("father")
    ;

    private String value;

    GlobalExceptionEnum(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
