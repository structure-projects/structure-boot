package cn.structure.starter.log.logback;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;

import java.sql.Timestamp;
import java.util.Map;

/**
 * <p>
 * json格式日志生成
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/6/3 12:05
 */
public class JSONLogLayout extends LayoutBase<ILoggingEvent> {

    private String MDCkey;

    private String projectName;

    @Override
    public String doLayout(ILoggingEvent event) {
        Map map = event.getMDCPropertyMap();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (map.get(MDCkey) != null) {
            sb.append("\"trace\":");
            sb.append("\"" + map.get(MDCkey) + "\", ");
        }
        sb.append("\"project\":");
        sb.append("\"" + projectName + "\", ");
        sb.append("\"pro_time\":");
        sb.append("\"").append(new Timestamp(event.getTimeStamp())).append("\"");
        sb.append(", \"level\":");
        sb.append("\"").append(event.getLevel()).append("\"");
        sb.append(", \"thread\":");
        sb.append("\"").append(event.getThreadName()).append("\"");
        sb.append(", \"class\": ");
        sb.append("\"").append(event.getLoggerName()).append("\"");
        sb.append(",\"message\": ");
        String message = event.getFormattedMessage();
        if (event.getThrowableProxy() != null) {
            ExtendedThrowableProxyConverter throwableConverter = new ExtendedThrowableProxyConverter();
            throwableConverter.start();
            message = event.getFormattedMessage() + "\n" + throwableConverter.convert(event);
            throwableConverter.stop();
        }
        sb.append("\"").append(message).append("\"");
        sb.append("}");
        sb.append(CoreConstants.LINE_SEPARATOR);
        return sb.toString();
    }

    public String getMDCkey() {
        return MDCkey;
    }

    public void setMDCkey(String MDCkey) {
        this.MDCkey = MDCkey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
