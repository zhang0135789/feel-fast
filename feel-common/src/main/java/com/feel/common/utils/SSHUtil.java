package com.feel.common.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

/**
 * ssh链接工具类
 */
public class SSHUtil {


	private static int timeout = 500000;

	public static  int DEFAULT_SSH_PORT = 22;

	private static final Logger logger = LoggerFactory.getLogger(SSHUtil.class);

	/**
	 * 密码方式登录
	 * @param ip
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Session ssh(String ip, String username, String password) throws Exception {
		logger.info("尝试连接到主机 {} ...",ip);
		logger.info("username:{},password:{}",username,password);
		JSch jsch = new JSch(); // 创建JSch对象
		Session session = jsch.getSession(username,ip,DEFAULT_SSH_PORT); // 根据用户名，主机ip，端口获取一个Session对象
		session.setPassword(password); // 设置密码
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		logger.info("主机 {} 连接成功!",ip);
		return session;
	}

	/**
	 * 文件方式登录
	 * @param ip
	 * @param username
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Session sshWithFile(String ip, String username, String file) throws Exception {
		return sshWithFile(ip,username,file,null);
	}


	/**
	 * 执行命令
	 * @param command
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> execute(String command,Session session) throws Exception {
		//保存输出内容的容器
		ArrayList<String> stdout=new ArrayList<>();
		//打开通道，设置通道类型，和执行的命令
		Channel channel = session.openChannel("exec");

		ChannelExec channelExec = (ChannelExec)channel;

		channelExec.setCommand(command);

		channelExec.setInputStream(null);
		BufferedReader input = new BufferedReader(new InputStreamReader
				(channelExec.getInputStream()));

		channelExec.connect();
		System.out.println("The remote command is :" + command);

		//接收远程服务器执行命令的结果
		String line;
		while ((line = input.readLine()) != null) {
			stdout.add(line);
		}
		input.close();
		// 关闭通道
		channelExec.disconnect();
		return stdout;

	}


	public static Session sshWithFile(String ip, String username, String file, Integer port) throws JSchException {
		logger.info("尝试连接到主机 {} ...",ip);
		logger.info("username:{},password:{}",username,file);
		JSch jsch = new JSch(); // 创建JSch对象
		//加载秘钥文件
		jsch.addIdentity(file);
		Session session = jsch.getSession(username,ip,port==null ? DEFAULT_SSH_PORT : port); // 根据用户名，主机ip，端口获取一个Session对象
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		logger.info("主机 {} 连接成功!",ip);
		return session;
	}
}
