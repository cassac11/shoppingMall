package shoppingmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shoppingmall.entity.SysConfig;
import shoppingmall.repository.SysConfigRepository;
import shoppingmall.utils.BusinessLayerResponse;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SysConfigService {
    
    @Autowired
    private SysConfigRepository sysConfigRepository;
    
    public SysConfig save(SysConfig sysConfig)
    {
        return sysConfigRepository.save(sysConfig);
    }
    
    public List<SysConfig> getAll()
    {
        return sysConfigRepository.findAll();
    }
    
    public String getContent(String id)
    {
        Optional<SysConfig> config = sysConfigRepository.findById(id);
        
        if (config.isPresent())
        {
            SysConfig sysConfig = config.get();
            
            return sysConfig.getContent();
        }
        return null;
    }
    
    // 新增設置訊息
    public BusinessLayerResponse<String> addItem(String id, String content)
    {
        if (!StringUtils.hasText(id))
            return BusinessLayerResponse.error("請輸入項目名稱");
        
        if (!StringUtils.hasText(content))
            return BusinessLayerResponse.error("請輸入項目內容");

        save(new SysConfig(id, content));
        
        return BusinessLayerResponse.ok("新增成功");
    }

    // 刪除設置訊息 
    public BusinessLayerResponse<String> deleteItem(String id)
    {
        Optional<SysConfig> sysConfig = sysConfigRepository.findById(id);
        sysConfig.ifPresent(config -> sysConfigRepository.delete(config));

        return BusinessLayerResponse.ok("刪除成功");
    }

    // 批量更新
    public BusinessLayerResponse<String> updateList(List<SysConfig> configs)
    {
        if (configs.isEmpty())
        {
            return BusinessLayerResponse.error("更新內容不可為空");
        }
        
        sysConfigRepository.saveAll(configs);
        
        return BusinessLayerResponse.ok("更新成功");
    }
    
    // 單一更改設置訊息
    public void updateItem(String id, String prop)
    {
        Optional<SysConfig> sysConfig = sysConfigRepository.findById(id);

        try
        {
            if (sysConfig.isPresent())
            {
                SysConfig config = sysConfig.get();
                config.setContent(prop);

                sysConfigRepository.save(config);
            }
        }
        catch (Exception ex)
        {
            log.error("SysConfigService ==> updateItem ... Exception : " + ex);
        }
    }
}
