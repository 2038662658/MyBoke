package com.cakes.frameworks.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cakes.frameworks.cache.CacheData;
import com.cakes.frameworks.service.CommonService;
import com.cakes.frameworks.weixin.wx.WxUtils;
import com.cakes.frameworks.weixin.wx.service.WxKeyReplyService;



/**
 * 系统数据初始化
 */
@Service
public class SysInitDataService {
	@Autowired
	private WxKeyReplyService wxKeyReplyService;
	@Autowired
	private CommonService cs;
	
	@Autowired
	private CacheData cache;

	@PostConstruct
	public void afterBeanInit() {
		System.out.println("系统数据初始化...");
		
//		 加载数据源列表
		loadDbMap();
//		loadWxData2Cache();
//		loadWxNoticeData2Cache();
		//加载缓存
		cache.initialize();
		
		WxUtils.initWechat();
	}
	
	private void loadDbMap(){
		//加载微信配置信息
		/*String sql="select pno_code,hello_cnt,def_cnt from wx_auto_reply ";
		List<Map<String, Object>> list=cs.queryBySql(sql);
		for(Map<String, Object> m:list){
			String pno_code=Methods.nvl(m.get("pno_code"));
			String hello_cnt=Methods.nvl(m.get("hello_cnt"));
			String def_cnt=Methods.nvl(m.get("def_cnt"));
			WxConstants.setHelloWorldText(pno_code, hello_cnt);
			WxConstants.setDefReplyText(pno_code, def_cnt);
		}
		loadKeyReply();*/
//		InputStream in = LoginAction.class.getClassLoader()
//				.getResourceAsStream("jdbc.properties");
//		Properties config = new Properties();
//		try {
//			config.load(in);
//		} catch (IOException e) {
//		} finally {
//			try {
//				in.close();
//			} catch (IOException e) {
//			}
//		}
//		String dbType = config.getProperty("jdbc.dbType");
//		String driver = config.getProperty("jdbc.driver");
//		config.clear();
//		String dbCode = "master";
//		Map<String, DBInfo> dbMap = new HashMap<String, DBInfo>();
//		DBInfo info = new DBInfo();
//		info.setDb_code(dbCode);
//		info.setDb_type(dbType);
//		info.setDriver(driver);
//		dbMap.put(dbCode, info);
//		List<BbDb> dbList= bbDbService.findAll();
//		for(BbDb db:dbList){
//			info = new DBInfo();
//			info.setDb_code(db.getDb_code());
//			info.setDb_type(db.getDb_type());
//			info.setDriver(db.getDriver());
//			dbMap.put(info.getDb_code(), info);
//		}
//		DBContextHolder.setDbMap(dbMap);
	}
	
	
	

	/*private void loadKeyReply() {
		String sql;
		
		sql="select * from wx_key_reply ";
		List<WxKeyReply> replyList=wxKeyReplyService.getEntityDao().simpleJdbcTemplate.query(sql, wxKeyReplyService.getEntityDao().getMapper());
		
		for(WxKeyReply reply:replyList){
			String pno_code=reply.getPno_code();
			WxConstants.setWxKeyReplyList(pno_code, new ArrayList<WxKeyReply>());
		}
		for(WxKeyReply reply:replyList){
			String pno_code=reply.getPno_code();
			List<WxKeyReply> tmpList=WxConstants.getWxKeyReplyList(pno_code);
			tmpList.add(reply);
		}
	}*/
}
