import com.alibaba.fastjson.JSONObject;
import com.singhand.seleniumcrawler.selenoium.Selenium;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/26 9:36
 */
public class JSONTest {


    public static void main(String[] args) {
        Selenium selenium=new Selenium();
        selenium.setTime(123123L);
        String s = JSONObject.toJSONString(selenium);

        System.out.println(s);
    }
}
