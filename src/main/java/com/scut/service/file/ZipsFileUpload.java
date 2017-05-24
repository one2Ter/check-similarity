package com.scut.service.file;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
/**
 * 多个jar包上传
 * @author jaybill
 *
 */
@Service
public class ZipsFileUpload extends AbstractFileUpload {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZipsFileUpload.class);
	public ZipsFileUpload(){}
	public ZipsFileUpload(MultipartFile file,String path){
		this.file = file;
		this.path = path;
	}
	@Override
	public String upload(MultipartFile[] files, String path) {
		path = path+File.separator+"zips";
		File dirFile = new File(path);
		if(!dirFile.exists()){
			dirFile.mkdirs();
		}
		//每上传一组，在里面新建一个文件夹，以当前上传的时间的时间戳为文件名
		String currentDirName = new Date().getTime()+"";		
		//上面新建的文件夹路径
		String currentDirPath = path+File.separator+currentDirName;
		//新建file关联当前文件夹路径
		File currentFile  = new File(currentDirPath);
		//创建文件夹
		currentFile.mkdirs();
		//上传
		for(int i=0;i<files.length;i++){
			cs.submit(new ZipsFileUpload(files[i],currentDirPath));//开启线程池，并行上传文件
		}
		//等待线程完成
		for(int i=0;i<files.length;i++){
			try {
				cs.take().get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("文件上传完成！");
		return currentDirPath;
	}
}
