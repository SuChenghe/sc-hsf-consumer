package com.aliware.edas.contorller;

import com.aliware.edas.service.ProvideTimeAsyncService;
import com.aliware.edas.service.ProvideTimeService;
import com.taobao.hsf.tbremoting.invoke.CallbackInvocationContext;
import com.taobao.hsf.tbremoting.invoke.HSFFuture;
import com.taobao.hsf.tbremoting.invoke.HSFResponseFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class GetTimeController {

    @Autowired
    private ProvideTimeService provideTimeService;
    @Autowired
    private ProvideTimeAsyncService provideTimeAsyncService;

    /**
     * 同步获取时间
     * @param str
     * @return
     */
    @RequestMapping(value = "/hsf-echo/{str}", method = RequestMethod.GET)
    public String getTime(@PathVariable String str) {
        return provideTimeService.getTime(str);
    }

    @RequestMapping(value = "/hsf-future/{str}", method = RequestMethod.GET)
    public String testFuture(@PathVariable String str) {
        String str1 = provideTimeAsyncService.getTimeFuture(str);
        String str2;
        try {
            HSFFuture hsfFuture = HSFResponseFuture.getFuture();
            str2 = (String) hsfFuture.getResponse(3000);
        } catch (Throwable t) {
            t.printStackTrace();
            str2 = "future-exception";
        }
        return str1 + "_" + str2;
    }

    @RequestMapping(value = "/hsf-future-list/{num}", method = RequestMethod.GET)
    public String testFutureList(@PathVariable int num) {
        try {
            List<HSFFuture> hsfFutures = new ArrayList<>();
            for (int i = 1; i <= num; i++) {
                provideTimeAsyncService.getTimeFuture(String.valueOf(i));
                hsfFutures.add(HSFResponseFuture.getFuture());
            }
            ArrayList<String> results = new ArrayList<String>();
            for (HSFFuture hsfFuture : hsfFutures) {
                results.add((String) hsfFuture.getResponse(3000));
            }
            return Arrays.toString(results.toArray());
        } catch (Throwable t) {
            return "exception";
        }
    }

    @RequestMapping(value = "/hsf-callback/{str}", method = RequestMethod.GET)
    public String testCallback(@PathVariable String str) {
        String timestamp = System.currentTimeMillis() + "-----";
        CallbackInvocationContext.setContext(timestamp);
        String str1 = provideTimeAsyncService.getTimeCallback(str);
        CallbackInvocationContext.setContext(timestamp + "------");
        return str1 + "  " + timestamp;
    }
}
