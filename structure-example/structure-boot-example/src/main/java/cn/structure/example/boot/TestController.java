package cn.structure.example.boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/12/5 22:55
 */
@RestController
@RequestMapping(value = "/")
public class TestController {

    @GetMapping(value = "test")
    public String test() {
        return "ok";
    }
}
