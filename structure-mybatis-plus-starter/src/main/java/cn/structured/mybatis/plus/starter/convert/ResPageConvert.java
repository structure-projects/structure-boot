/*
Copyright 2025 Structure Projects

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package cn.structured.mybatis.plus.starter.convert;

import cn.structure.common.vo.ResPage;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 类型转化工具类
 *
 * @author chuck
 * @version 1.0
 * @since 1.8
 * @since 2026/1/5-下午9:57
 */
public class ResPageConvert {

    /**
     * 转换
     *
     * @param page     分页
     * @param function 转换方法
     * @param <R>      转换类型
     * @param <P>      原类型
     * @return 转换后的分页
     */
    public static <R, P> ResPage<R> convert(IPage<P> page, Function<P, R> function) {
        ResPage<R> resPageDTO = new ResPage<>();
        resPageDTO.setCurrent(page.getCurrent());
        resPageDTO.setPages(page.getPages());
        resPageDTO.setSize(page.getSize());
        resPageDTO.setTotal(page.getTotal());
        List<R> records = new ArrayList<>();
        page.getRecords().forEach(p -> {
            R apply = function.apply(p);
            records.add(apply);
        });
        resPageDTO.setRecords(records);
        return resPageDTO;
    }
}
