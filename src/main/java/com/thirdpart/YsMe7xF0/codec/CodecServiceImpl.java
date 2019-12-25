package com.thirdpart.YsMe7xF0.codec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.h3c.iot.codec.AnalysisUpResult;
import com.h3c.iot.codec.CodecService;
import com.h3c.iot.codec.ConfirmResult;
import com.h3c.iot.codec.ReportResult;
import com.h3c.iot.codec.SpliceDownResult;
import com.h3c.iot.codec.ReportResult.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/*请在这里实现编解码逻辑，出入参格式及要求要求请参照开发规范；
这里涉及到的properyTopic,propertyName,identifier为H3C物联网平台参数标准（请参考 开发者中心 > 终端定制 > 功能信息 > 功能列表JSON视图）；
reportid，confirmid等参数是厂商自己的参数，开发者需要根据自家厂商文档或规范完成与H3C标准的互相转换*/
public class CodecServiceImpl implements CodecService {

	@Override
	public AnalysisUpResult analysisUp(String message) throws Exception {
		//报文消息id，各厂商可能格式有所不同
		int messageId = Integer.parseInt(message.substring(20, 22), 16);
		System.out.println("messageId=" + messageId);

        ReportResult reportResult = new ReportResult();

        List<String> messageList = subMessage(messageId, message.substring(24, (message.length() - 6)));
        System.out.println("messageList=" + messageList);

		if (message.indexOf("030180") > -1) {
            //参数数据
            messageList.forEach(mess -> {
                System.out.println("mess=" + mess);
                int type = Integer.parseInt(mess.substring(0, 2), 16);
                System.out.println("type=" + type);

                switch (type) {
                    case 0x03:
                        reportResult.put("13", new Property("终端类型", "TBS-101"));
                        break;
                    case 0x05:
                        reportResult.put("14", new Property("软硬件版本", (mess.substring(5, 6) + "-" + mess.substring(4, 6))));
                        break;
                    case 0x06:
                        reportResult.put("0x10800075", new Property("正常工作时间间隔", Integer.parseInt(mess.substring(4, 12), 16)));
                        break;
                    case 0x08:
                        reportResult.put("0x1080006D", new Property("紧急报警间隔", Integer.parseInt(mess.substring(4, 12), 16)));
                        break;
                    case 0x09:
                        reportResult.put("0x10800076", new Property("一般报警间隔", Integer.parseInt(mess.substring(4, 12), 16)));
                        break;
                    case 0x14:
                        reportResult.put("0x1080006F", new Property("浓度阈值", Integer.parseInt(mess.substring(4, 8), 16)));
                        break;
                    case 0x0b:
                        reportResult.put("0x10800070", new Property("温度报警值上限", Integer.parseInt(mess.substring(4, 6), 16)));
                        break;
                    case 0x21:
                        reportResult.put("0x10800071", new Property("温度报警值下限", parseNegative(Integer.parseInt(mess.substring(4, 6), 16))));
                        break;
                    default:
                        break;
                }
            });
		} else {
            //状态数据
            messageList.forEach(mess -> {
                System.out.println("mess=" + mess);
                int type = Integer.parseInt(mess.substring(0, 2), 16);
                System.out.println("type=" + type);

                switch (type) {
                    case 0x01:
                        if ("1".equals(mess.substring(5, 6))) {
                            reportResult.put("0x1080007A", new Property("火灾报警", "火灾报警"));
                        } else if ("2".equals((mess.substring(5, 6)))) {
                            reportResult.put("0x1080007B", new Property("温度报警", "温度报警"));
                        }
                        break;
                    case 0x02:
                        if ("2".equals((mess.substring(5, 6)))) {
                            reportResult.put("0x1080007C", new Property("烟感电量低", "烟感电量低"));
                        } else if ("3".equals(mess.substring(5, 6))) {
                            reportResult.put("0x1080007D", new Property("防拆报警", "防拆报警"));
                        } else if ("7".equals(mess.substring(5, 6))) {
                            reportResult.put("0x1080007E", new Property("发射电路故障", "发射电路故障"));
                        }
                        break;
                    case 0x0b:
                        reportResult.put("0x10800079", new Property("温度值", Integer.parseInt(mess.substring(4, 6), 16)));
                        break;
                    case 0x14:
                        reportResult.put("0x10800078", new Property("烟雾浓度值", Integer.parseInt(mess.substring(4, 8), 16)));
                        break;
                    case 0x17:
                        reportResult.put("0x10800077", new Property("发送失败帧计数", Integer.parseInt(mess.substring(4, 8), 16)));
                        break;
                    case 0x19:
                        reportResult.put("4", new Property("设备自检", Integer.parseInt(mess.substring(4, 6), 16)));
                        break;
                    case 0x24:
                        reportResult.put("0x10800074", new Property("设备电量", Integer.parseInt(mess.substring(4, 6), 16)));
                        break;
                    default:
                        break;
                }
            });
		}

		return AnalysisUpResult.report(reportResult);
	}


	@Override
	public SpliceDownResult spliceDown(String property) throws Exception {
		return SpliceDownResult.unNeedAckPayload("7e01000000000000000304000c010100007e");
	}

	private List<String> subMessage(int id, String message) {
		List<String> messageList = new ArrayList<String>();
		System.out.println("sub_message=" + message);

		while (message.length() > 2) {
		    int type = Integer.parseInt(message.substring(0, 2), 16);
		    switch (type) {
                case 0x03:
                case 0x05:
		        case 0x0b:
                case 0x19:
                case 0x21:
                case 0x24:
                    messageList.add(message.substring(0, 6));
                    message = message.substring(6);
                    break;
                case 0x14:
                case 0x17:
                    messageList.add(message.substring(0, 8));
                    message = message.substring(8);
                    break;
                case 0x06:
                case 0x08:
                case 0x09:
                    messageList.add(message.substring(0, 12));
                    message = message.substring(12);
                    break;
                default:
                    message = "";
                    break;
				}
			}

		return messageList;
	}

	private int parseNegative(int number) {
	    return -Integer.parseInt(Integer.toBinaryString(~(number-1)).substring(24),2);
    }
}
