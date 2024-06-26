package cn.structured.mybatis.plus.starter.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * <p>
 * 出参分页
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/6/26 13:45
 */
@Data
public class ResPage<T> implements Serializable {

    private Long current;
    private Long pages;
    private List<T> records;
    private Long size;
    private Long total;

    public static <R, P> ResPage<R> convert(IPage<P> page, Function<P, R> function) {
        ResPage<R> resPageDTO = new ResPage<>();
        resPageDTO.setCurrent(page.getCurrent());
        resPageDTO.setPages(page.getPages());
        resPageDTO.setSize(page.getSize());
        resPageDTO.setTotal(page.getTotal());
        List<R> records = new ArrayList<>();
        page.getRecords().stream().forEach(p -> {
            R apply = function.apply(p);
            records.add(apply);
        });
        resPageDTO.setRecords(records);
        return resPageDTO;
    }
}
