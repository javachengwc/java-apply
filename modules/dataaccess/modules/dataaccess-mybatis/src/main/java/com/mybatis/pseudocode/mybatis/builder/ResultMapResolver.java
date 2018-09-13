package com.mybatis.pseudocode.mybatis.builder;

import com.mybatis.pseudocode.mybatis.mapping.ResultMap;
import com.mybatis.pseudocode.mybatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.Discriminator;

import java.util.List;

//ResultMapResolver的作用是生成ResultMap对象
public class ResultMapResolver
{
    private final MapperBuilderAssistant assistant;
    private final String id;
    private final Class<?> type;
    private final String extend;
    private final Discriminator discriminator;
    private final List<ResultMapping> resultMappings;

    private final Boolean autoMapping;

    public ResultMapResolver(MapperBuilderAssistant assistant, String id, Class<?> type, String extend,
                             Discriminator discriminator, List<ResultMapping> resultMappings, Boolean autoMapping)
    {
        this.assistant = assistant;
        this.id = id;
        this.type = type;
        this.extend = extend;
        this.discriminator = discriminator;
        this.resultMappings = resultMappings;
        this.autoMapping = autoMapping;
    }

    //构造ResultMap对象，并将其存入Configuration对象的resultMaps容器中。
    public ResultMap resolve() {
        return this.assistant.addResultMap(this.id, this.type, this.extend, this.discriminator, this.resultMappings, this.autoMapping);
    }
}
