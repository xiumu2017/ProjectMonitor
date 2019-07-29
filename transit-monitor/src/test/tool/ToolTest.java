package tool;

import com.paradise.TransitMonitorApplication;
import com.paradise.monitor.MonitorTools;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest(classes = TransitMonitorApplication.class)
@RunWith(SpringRunner.class)
public class ToolTest {

    @Resource
    private MonitorTools monitorTools;

    @Test
    public void test() {
        try {
            if (monitorTools != null) {
                monitorTools.run();
            } else {
                log.info(" why ");
            }
        } catch (InterruptedException e) {
            log.error(e.getLocalizedMessage());
        }
    }
}
