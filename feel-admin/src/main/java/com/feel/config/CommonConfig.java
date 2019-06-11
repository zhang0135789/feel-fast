package com.feel.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yml"}, ignoreResourceNotFound = true)
public class CommonConfig {

	public static String NODENO = "ps13452aw";

	public static int SLEEP_TIME = 1000;

	public static String toViewString() {
		return "\r\nNODENO[" + NODENO + "]\r\n";
	}


	//websocket客户端链接名
	public static String WSClientName = "ChainService";
	//explorer 链接名
	public static String ExplorerName = "explorer";






	//websocket链接信息
	//是否使用https
	private static String ws_protocol;
	//websocket url
	private static String wsUrl;
	//版本号
	private static String vers;


	//explor  socketio 通讯
	private static String socketio;


	public static String getVers() {
		return vers;
	}

	@Value("${client.wsVers}")
	public void setVers(String vers) {
		this.vers = vers;
	}

	public static String getSocketio() {
		return socketio;
	}

	@Value("${client.socketio}")
	public void setSocketio(String socketio) {
		this.socketio = socketio;
	}

	public static String getWs_protocol() {
		return ws_protocol;
	}

	@Value("${client.ws_protocol}")
	public void setWs_protocol(String ws_protocol) {
		this.ws_protocol = ws_protocol;
	}

	public static String getWsUrl() {
		return wsUrl;
	}

	@Value("${client.wsUrl}")
	public void setWsUrl(String wsUrl) {
		this.wsUrl = wsUrl;
	}
}
