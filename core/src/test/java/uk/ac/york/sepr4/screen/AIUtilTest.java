package uk.ac.york.sepr4.screen;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.york.sepr4.utils.AIUtil;

public class AIUtilTest {

    @Test
    public void normalizeAngleTest() {
        Assert.assertEquals(-Math.PI, AIUtil.normalizeAngle((float) (-3*Math.PI)), 0.01);

        Assert.assertEquals(Math.PI, AIUtil.normalizeAngle((float) (3*Math.PI)), 0.01);
    }
}