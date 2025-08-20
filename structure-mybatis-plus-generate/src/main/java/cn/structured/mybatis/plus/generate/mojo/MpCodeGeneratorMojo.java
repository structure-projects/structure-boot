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
package cn.structured.mybatis.plus.generate.mojo;

import cn.structured.mybatis.plus.generate.config.MpCodeGeneratorConfig;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chuck
 */
@Mojo(name = "generator", defaultPhase = LifecyclePhase.COMPILE)
public class MpCodeGeneratorMojo extends AbstractMojo {

    private static final String DEFAULT_PATH = "mp-code-generator-config.yaml";

    @Parameter
    private String configurationFile;

    @Override
    public void execute() {
        InputStream inputStream = null;
        if (!StringUtils.isEmpty(configurationFile)) {
            try {
                inputStream = new FileInputStream(configurationFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            inputStream = MpCodeGeneratorMojo.class.getClassLoader().getResourceAsStream(DEFAULT_PATH);
        }

        MpCodeGeneratorConfig config = yaml2Config(inputStream);

        Map<String, FieldFill> tableFill = config.getTableFill();
        if (null != tableFill && tableFill.size() > 0) {
            List<TableFill> tableFillList = new ArrayList<TableFill>();
            tableFill.forEach((k, v) -> {
                tableFillList.add(new TableFill(k, v));
            });
            config.getStrategyConfig().setTableFillList(tableFillList);
        }

        AutoGenerator mpg = configureAutoGenerator(config);
        mpg.execute();
    }


    private MpCodeGeneratorConfig yaml2Config(InputStream inputStream) {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml.loadAs(inputStream, MpCodeGeneratorConfig.class);
    }

    private AutoGenerator configureAutoGenerator(MpCodeGeneratorConfig config) {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(config.getGlobalConfig());
        mpg.setDataSource(config.getDataSourceConfig());
        mpg.setPackageInfo(config.getPackageConfig());
        mpg.setTemplate(new TemplateConfig());
        mpg.setStrategy(config.getStrategyConfig());
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        return mpg;
    }
}
