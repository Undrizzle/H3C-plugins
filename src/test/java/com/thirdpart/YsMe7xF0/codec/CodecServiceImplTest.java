package com.thirdpart.YsMe7xF0.codec;

import com.google.gson.Gson;
import com.h3c.iot.codec.AnalysisUpResult;
import com.h3c.iot.codec.CodecService;
import com.h3c.iot.codec.SpliceDownResult;
import org.junit.Before;
import org.junit.Test;

/*在这里进行编解码测试*/
public class CodecServiceImplTest {

	private CodecService codecService;

	@Before
	public void setCodecService() {
		this.codecService = new CodecServiceImpl();
	}

	@Test
	public void testAnalysisUp() throws Exception {
		String message1 = "7e010000000000000022010003018005011406040000a8c008040000001e09040000a8c0140200640b01322101f64ba97e";
		AnalysisUpResult analysisUpResult1 = codecService.analysisUp(message1);
		System.out.println(new Gson().toJson(analysisUpResult1));
		String message2 = "7e01000000000010000e0100170200dd140200000b010d24016418747e";
		AnalysisUpResult analysisUpResult2 = codecService.analysisUp(message2);
		System.out.println(new Gson().toJson(analysisUpResult2));
	}

	@Test
	public void testSpliceDown() throws Exception {
		String property = "{\"0x1080030D\":{\"MinTime\":\"3600\",\"MaxTime\":\"4371\",\"BatteryChange\":\"18\"}}";
		SpliceDownResult spliceDownResult = codecService.spliceDown(property);
		System.out.println(new Gson().toJson(spliceDownResult));
	}

}
