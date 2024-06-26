package cn.structure.example.web.restful.controller;

import cn.structure.common.entity.ResResultVO;
import cn.structure.common.exception.CommonException;
import cn.structure.common.utils.IResultUtil;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.example.web.restful.pojo.vo.ReqTestVO;
import cn.structure.example.web.restful.pojo.vo.ResTestVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


/**
 * <p>
 * 测试RestConteoller
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 21:35
 */
@Validated
@RestController
@RequestMapping(value = "/test")
public class RestTestController {

    @Resource
    private IResultUtil iResultUtil;

    @PostMapping(value = "/post")
    public ResResultVO<ResTestVO> postTest(@Valid @RequestBody ReqTestVO reqTestVO) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(reqTestVO.getId());
        resTestVO.setName(reqTestVO.getName());
        return ResultUtilSimpleImpl.success(resTestVO);
    }

    @GetMapping(value = "/get")
    public ResResultVO<ResTestVO> getTest(@NotBlank(message = "this is id not blank") @RequestParam("id") String id) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(id);
        resTestVO.setName("testId");
        return ResultUtilSimpleImpl.success(resTestVO);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResResultVO<ResTestVO> delete(@PathVariable("id") String id) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(id);
        resTestVO.setName("testId");
        return ResultUtilSimpleImpl.success(resTestVO);
    }

    @PutMapping(value = "/put/{id}")
    public ResResultVO<ResTestVO> put(@PathVariable("id") String id, @RequestBody @Validated ReqTestVO reqTestVO) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(id);
        resTestVO.setName(reqTestVO.getName());
        return ResultUtilSimpleImpl.success(resTestVO);
    }

    @RequestMapping(value = "/exception")
    public ResResultVO<ResTestVO> exception() {
        return ResultUtilSimpleImpl.fail("12001", "测试异常", null);
    }

    @RequestMapping(value = "/fail")
    public ResResultVO<ResTestVO> fail(ReqTestVO reqTestVO) {
        if (null == reqTestVO.getId()) {
            throw new CommonException("12001", "测试异常");
        }
        return ResultUtilSimpleImpl.success(null);

    }

}
