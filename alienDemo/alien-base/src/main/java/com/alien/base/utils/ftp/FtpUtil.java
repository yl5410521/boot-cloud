package com.alien.base.utils.ftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alien.base.config.FtpConfig;



/**
 * 类说明：文件上传下载工具类
 * 
 */
public class FtpUtil {
	public static final int imageCutSize = 300;
	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

	private final static String DIRSPLIT = "/";

	// 下载的文件目录
	@SuppressWarnings("unused")
	private String downloadDir;

	// ftp客户端
	private FTPClient ftpClient = new FTPClient();

	/**
	 * 
	 * 功能：上传文件附件到文件服务器
	 * 
	 * @param buffIn:上传文件流
	 * @param fileName：保存文件名称
	 * @param needDelete：是否同时删除
	 * @return
	 * @throws IOException
	 */
	public String uploadToFtp(FtpConfig ftpConfig, InputStream buffIn, String fileName, boolean needDelete)
			throws FTPConnectionClosedException, IOException, Exception {

		boolean returnValue = false;
		InputStream inputStream = null;

		// 上传文件
		try {

			// 建立连接
			connectToServer(ftpConfig);

			
			
			 if (needDelete) {
				 ftpClient.changeWorkingDirectory(ftpConfig.getFilepath());
			 ftpClient.removeDirectory(fileName);
			 //ftpClient.completePendingCommand(); 
			 }
			 

			// 设置传输二进制文件
			setFileType(FTP.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("failed to connect to the FTP Server:" + ftpConfig.getHost());
			}
			ftpClient.enterLocalPassiveMode();

			if (StringUtils.isBlank(ftpConfig.getFilepath())) {

				if (!existDirectory(ftpConfig.getFilepath())) {
					this.createDirectory(ftpConfig.getFilepath());
				}
				ftpClient.changeWorkingDirectory(ftpConfig.getFilepath());
			} else {
				ftpClient.makeDirectory(ftpConfig.getFilepath());
				ftpClient.changeWorkingDirectory(ftpConfig.getFilepath());
			}
			// 上传文件到ftp
			returnValue = ftpClient.storeFile(fileName, buffIn);
			// 输出操作结果信息
			if (returnValue) {
				log.info("uploadToFtp INFO: upload file  to ftp : succeed!");
			} else {
				log.info("uploadToFtp INFO: upload file  to ftp : failed!");
			}
			//5.遍历下载的目录
	           FTPFile[] fs = ftpClient.listFiles();
	           for (FTPFile ff : fs){
	               //解决中文乱码问题，两次解码
	               byte[] bytes=ff.getName().getBytes("iso-8859-1");
	               String file=new String(bytes,"utf-8");
	               if(file.equals(fileName)){
	                   inputStream = ftpClient.retrieveFileStream(fileName);
	               }
	           }
	       
         if (inputStream != null) {
             byte[] data=null;
             ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
             byte[] buff = new byte[100];
             int rc = 0;
             while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                 swapStream.write(buff, 0, rc);
             }
             data = swapStream.toByteArray();
            // data=new byte[inputStream.available()];
             inputStream.read(data);
             inputStream.close();
         
             	return new String(Base64.encodeBase64(data));
         }

			buffIn.close();

			// 关闭连接
			closeConnect();
		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		} catch (Exception e) {
			returnValue = false;
			log.error("ERR : upload file  to ftp : failed! ", e);
			throw e;
		} finally {
			try {
				if (buffIn != null) {
					buffIn.close();
				}
			} catch (Exception e) {
				log.error("ftp关闭输入流时失败！", e);
			}
			if (ftpClient.isConnected()) {
				closeConnect();
			}
		}
		return null;
	}

	/**
	 * 
	 * 功能：根据文件名称，下载文件流
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public InputStream downloadFile(FtpConfig ftpConfig, String filename) throws IOException {
		InputStream in = null;
		try {

			// 建立连接
			connectToServer(ftpConfig);
			ftpClient.enterLocalPassiveMode();
			// 设置传输二进制文件
			setFileType(FTP.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("failed to connect to the FTP Server:" + ftpConfig.getHost());
			}
			ftpClient.changeWorkingDirectory(ftpConfig.getFilepath());

			// ftp文件获取文件
			in = ftpClient.retrieveFileStream(filename);

		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		} catch (Exception e) {
			log.error("ERR : upload file " + filename + " from ftp : failed!", e);
		}
		return in;
	}

	/**
	 * 删除文件
	 * 
	 * @param pathname
	 * @param filename
	 * @return
	 */
	public   boolean deleteFile(FtpConfig ftpConfig, String filename) {
		boolean flag = false;
		try {
			System.out.println("开始删除文件");
			connectToServer(ftpConfig);
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(ftpConfig.getFilepath());
			ftpClient.deleteFile(filename);
			//ftpClient.completePendingCommand();
		  //  ftpClient.
			ftpClient.logout();
			 ftpClient.disconnect();
			flag = true;
			System.out.println("删除文件成功");
		} catch (Exception e) {
			System.out.println("删除文件失败");
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 转码[GBK -> ISO-8859-1] 不同的平台需要不同的转码
	 * 
	 * @param obj
	 * @return
	 */
//    private String gbkToIso8859(Object obj) {
//        try {
//            if (obj == null)
//                return "";
//            else
//                return new String(obj.toString().getBytes("GBK"), "iso-8859-1");
//        } catch (Exception e) {
//            return "";
//        }
//    }

	/**
	 * 设置传输文件的类型[文本文件或者二进制文件]
	 * 
	 * @param fileType --BINARY_FILE_TYPE、ASCII_FILE_TYPE
	 */
	private void setFileType(int fileType) {
		try {
			ftpClient.setFileType(fileType);
		} catch (Exception e) {
			log.error("ftp设置传输文件的类型时失败！", e);
		}
	}

	/**
	 * 
	 * 功能：关闭连接
	 */
	public void closeConnect() {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			log.error("ftp连接关闭失败！", e);
		}
	}

	/**
	 * 连接到ftp服务器
	 */
	private void connectToServer(FtpConfig ftpConfig) throws FTPConnectionClosedException, Exception {
		if (!ftpClient.isConnected()) {
			int reply;
			try {
				ftpClient = new FTPClient();
				ftpClient.connect(ftpConfig.getHost(), ftpConfig.getPort());
				ftpClient.login(ftpConfig.getUsername(), ftpConfig.getPassword());
				reply = ftpClient.getReplyCode();

				if (!FTPReply.isPositiveCompletion(reply)) {
					ftpClient.disconnect();
					log.info("connectToServer FTP server refused connection.");
				}

			} catch (FTPConnectionClosedException ex) {
				log.error("服务器:IP：" + ftpConfig.getHost() + "没有连接数！there are too many connected users,please try later",
						ex);
				throw ex;
			} catch (Exception e) {
				log.error("登录ftp服务器【" + ftpConfig.getHost() + "】失败", e);
				throw e;
			}
		}
	}

	// Check the path is exist; exist return true, else false.
	public boolean existDirectory(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 创建FTP文件夹目录
	 * 
	 * @param pathName
	 * @return
	 * @throws IOException
	 */
	public boolean createDirectory(String pathName) throws IOException {
		boolean isSuccess = false;
		try {
			isSuccess = ftpClient.makeDirectory(pathName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 带点的
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	/**
	 * 不带点
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getNoPointExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos + 1);
	}

	/**
	 * 
	 * 功能：根据当前时间获取文件目录
	 * 
	 * @return String
	 */
	public static String getDateDir(Date dateParam) {
		Calendar cal = Calendar.getInstance();
		if (null != dateParam) {
			cal.setTime(dateParam);
		}
		int currentYear = cal.get(Calendar.YEAR);
		int currentMouth = cal.get(Calendar.MONTH) + 1;
		int currentDay = cal.get(Calendar.DAY_OF_MONTH);
		// int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		// return
		// currentYear+FtpOperation.DIRSPLIT+currentMouth+FtpOperation.DIRSPLIT+currentDay+FtpOperation.DIRSPLIT+currentHour;
		return currentYear + FtpUtil.DIRSPLIT + currentMouth + FtpUtil.DIRSPLIT + currentDay;
	}
	
	/**
	 * 读取FTP文件并转换base64编码
	 * @param inn
	 * @param pages
	 * @param lTime
	 * @param request
	 * @param filesN
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public synchronized String copyFileByBase64(FtpConfig ftpConfig,String fileName) throws Exception {
		String image = null;
		InputStream inputStream = null;
		try {

			// 建立连接
			connectToServer(ftpConfig);
			ftpClient.enterLocalPassiveMode();
			// 设置传输二进制文件
			setFileType(FTP.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				throw new IOException("failed to connect to the FTP Server:" + ftpConfig.getHost());
			}
			ftpClient.changeWorkingDirectory(ftpConfig.getFilepath()); //获取文件流
			//5.遍历下载的目录
	           FTPFile[] fs = ftpClient.listFiles();
	           for (FTPFile ff : fs){
	               //解决中文乱码问题，两次解码
	               byte[] bytes=ff.getName().getBytes("iso-8859-1");
	               String file=new String(bytes,"utf-8");
	               if(file.equals(fileName)){
	                   inputStream = ftpClient.retrieveFileStream(fileName);
	               }
	           }
	       
            if (inputStream != null) {
                byte[] data=null;
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[100];
                int rc = 0;
                while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                data = swapStream.toByteArray();
               // data=new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
            
                	return new String(Base64.encodeBase64(data));
            }

		} catch (FTPConnectionClosedException e) {
			log.error("ftp连接被关闭！", e);
			throw e;
		} catch (Exception e) {
			log.error("ERR : copyFileByBase64 file " + fileName + " from ftp : failed!", e);
		}finally {
			 if(ftpClient!=null&&ftpClient.isConnected())
		      {
		         try {
		            ftpClient.disconnect();
		         } catch (IOException e) {
		            e.printStackTrace();
		         }
		      }
		
		}
	 
		
		return "data:image/jpg;base64,"+image;
	}

}