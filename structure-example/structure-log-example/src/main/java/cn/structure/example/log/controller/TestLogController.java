package cn.structure.example.log.controller;

import cn.structure.example.log.pojo.vo.ReqTestVO;
import cn.structure.example.log.pojo.vo.ResTestVO;
import cn.structure.starter.log.anno.AspectParamLog;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * controller日志测试
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 14:14
 */
@RequestMapping("/test")
@RestController
public class TestLogController {

    /**
     * 使用 AspectParamLog 对方法的日志进行记录
     * @param reqTestVO
     * @return
     */
    @AspectParamLog()
    @RequestMapping(value = "test")
    public ResTestVO testLog (@RequestBody ReqTestVO reqTestVO) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(reqTestVO.getId());
        resTestVO.setName(reqTestVO.getName());
        return resTestVO;
    }

}
