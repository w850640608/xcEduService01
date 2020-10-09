package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author wzg
 * @Date 2020/6/28
 * @Version 1.0
 */
@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsConfigRepository cmsConfigRepository;
    @Autowired
    CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    /**
     * 页面列表分页查询
     *
     * @param page             当前页码
     * @param size             页面显示个数
     * @param queryPageRequest 查询条件
     * @return 页面列表
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //自定义分页查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //设置条件值<站点id>
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());

        }
        //设置模板id作为条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());

        }
        //页面别名作为条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());

        }
        //定义条件对象
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;//为了适应mogondb接口页数减一
        //分页对象
        Pageable pageable = new PageRequest(page, size);
        //分页查询
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult<CmsPage> cmsPageQueryResult = new QueryResult<CmsPage>();
        cmsPageQueryResult.setList(all.getContent());//数据列表
        cmsPageQueryResult.setTotal(all.getTotalElements());//数据总记录数
        return new QueryResponseResult(CommonCode.SUCCESS, cmsPageQueryResult);
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //抛出异常，非法参数异常..指定异常信息的内容
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //校验页面名称、站点Id、页面webpath的唯一性
        //根据页面名称、站点Id、页面webpath去cms_page集合，如果查到说明此页面已经存在，如果查询不到再继续添加
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1!= null) {
            //页面已经存在
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

             //调用dao新增页面
             cmsPage.setPageId(null);
             cmsPageRepository.save(cmsPage);
             return new CmsPageResult(CommonCode.SUCCESS, cmsPage);


    }
    //根据id查询页面
    public CmsPage getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        System.out.println(optional);
        if (optional.isPresent()){//optional对象不为null
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }
    //修改页面
    public CmsPageResult update(String id,CmsPage cmsPage){
        //根据id查询数据库页面信息
        CmsPage one = this.getById(id);
        if(one!=null){
            //准备更新数据
            //设置要修改的数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新dataUrl
            one.setDataUrl(cmsPage.getDataUrl());
            //提交修改
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS,one);
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL,null);

    }
    public ResponseResult del(String  id){
            CmsPage one = this.getById(id);
            if(one!=null){
                cmsPageRepository.deleteById(id);
                return new ResponseResult(CommonCode.SUCCESS);
            }
            return new ResponseResult(CommonCode.FAIL);
    }
    //根据id查询cmsConfig
    public CmsConfig getConfigById(@PathVariable String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if(optional.isPresent()){
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }
    //页面静态化方法
    /**
     * 静态化程序获取页面的DataUrl
     *
     * 静态化程序远程请求DataUrl获取数据模型。
     *
     * 静态化程序获取页面的模板信息
     *
     * 执行页面静态化
     */
    public String getPageHtml(String pageId){
        //获取数据模型
        Map model = getModelByPageId(pageId);
        if(model == null){
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
       System.out.println(model);
        //获取页面的模板信息
        String template = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        String html = generateHtml(template, model);
        return html;

    }
    //执行静态化
    private String generateHtml(String templateContent,Map model ){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");
            //调用api进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取页面的模板信息
    private String getTemplateByPageId(String pageId){
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面的模板id
        String templateId = cmsPage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if(optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //从GridFS中取模板文件内容
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));

            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    //获取数据模型
    private Map getModelByPageId(String pageId){
        //取出页面的信息
        System.out.println("3");
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        System.out.println(cmsPage);
        //取出页面的dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            //页面dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;

    }

    //页面发布
    public ResponseResult post(String pageId){
        //执行静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面存到GridFs
        CmsPage cmsPage = this.saveHtml(pageId,pageHtml);
        //向mq发送消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);


    }
    //保存html到GridFs
    private CmsPage saveHtml(String pageId,String htmlContent) {
        //拿到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //将htmlContent内容转化为输入流
        InputStream inputStream = null;
        ObjectId objectId = null;
        try {
            inputStream = IOUtils.toInputStream(htmlContent, "UTF-8");
            //将html保存到GridFs
             objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }
    //向mq发送消息
    private void sendPostPage(String pageId){
        //拿到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if(cmsPage == null){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //创建消息对象
        Map<String,String> msg =new HashMap<>();
        msg.put("pageId",pageId);
        //转成json字符串
        String jsonString = JSON.toJSONString(msg);
        //拿到站点id
        String siteId = cmsPage.getSiteId();
        //发送给mq
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,jsonString);

    }

    /**
     * 保存页面有就更新，没有就保存
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage) {
        //判断页面是否存在
        CmsPage one = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if(one!=null){
            //进行更新
            return this.update(one.getPageId(),cmsPage);
        }
        return this.add(cmsPage);

    }

    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //将页面信息存储到cms_page集合当中
        CmsPageResult save = this.save(cmsPage);
        if(!save.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //得到页面id
        CmsPage cmsPageSave = save.getCmsPage();
        String pageId = save.getCmsPage().getPageId();
        //执行页面发布（先静态化保存到GridFs,想mq发送消息）
       ResponseResult post = this.post(pageId);
        if(!post.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //拼接页面url=
        String siteId = cmsPageSave.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        String pageUrl = cmsSite.getSiteDomain()+cmsSite.getSiteWebPath()+cmsPageSave.getPageWebPath()+cmsPageSave.getPageName();
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }
    //根据id查询站点信息
    public CmsSite findCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }return null;
    }
}
