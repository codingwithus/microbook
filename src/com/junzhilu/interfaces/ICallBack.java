package com.junzhilu.interfaces;

import java.util.Map;

/*
 * 回调接口类，需要处理异步消息的Activity均需实现该类
 */
public interface ICallBack {

	/*
	 * 执行回调操作 map是回调的参数接口 name: 是响应时间的名称，上层应用根据name进行不同的操作
	 */
	void doCallBack(Map<String, Object> map);
}
