/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    /**
     * 转换
     *
     * @param page      分页
     * @param function  转换方法
     * @param <R>       转换类型
     * @param <P>       原类型
     * @return 转换后的分页
     */
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
