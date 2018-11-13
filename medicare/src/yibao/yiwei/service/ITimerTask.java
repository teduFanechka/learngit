package yibao.yiwei.service;

import yibao.yiwei.entity.system.Uploadfile;

public interface ITimerTask {

	/**
	 * 解析文件
	 * @param uploadfile
	 */
	public void parseFile(Uploadfile uploadfile);
	
	/**
	 * 定点上传统计生成
	 */
	public void couUploadfile();
	
	/**
	 * 停止定点
	 */
	public void stopTzddlog();
	
}
