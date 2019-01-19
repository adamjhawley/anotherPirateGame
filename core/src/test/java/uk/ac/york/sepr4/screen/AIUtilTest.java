package uk.ac.york.sepr4.screen;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.york.sepr4.utils.AIUtil;

public class AIUtilTest {

    @Test
    public void convertToRealAngleTest() {
        Assert.assertEquals(2.035, AIUtil.convertToRealAngle(90), 0.01);

        Assert.assertEquals(4.245, AIUtil.convertToRealAngle(-90), 0.01);
    }
}