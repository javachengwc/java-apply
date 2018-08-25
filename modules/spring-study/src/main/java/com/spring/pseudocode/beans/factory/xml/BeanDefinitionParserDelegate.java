package com.spring.pseudocode.beans.factory.xml;

import com.spring.pseudocode.beans.factory.config.BeanDefinition;
import com.spring.pseudocode.beans.factory.config.BeanDefinitionHolder;
import com.spring.pseudocode.core.core.env.Environment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.parsing.BeanEntry;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.DocumentDefaultsDefinition;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.*;

//spring的xml配置文件的扩展的解析处理由此bean定义解析委派类BeanDefinitionParserDelegate完成
//BeanDefinitionParserDelegate将bean的各种标签解析成BeanDefinition对象，并组装成BeanDefinitionHolder返回
public class BeanDefinitionParserDelegate
{
    //BeanDefinitionParserDelegate中定义的spring的标签元素
    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
    public static final String TRUE_VALUE = "true";
    public static final String FALSE_VALUE = "false";
    public static final String DEFAULT_VALUE = "default";
    public static final String DESCRIPTION_ELEMENT = "description";
    public static final String AUTOWIRE_NO_VALUE = "no";
    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
    public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
    public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";
    public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";
    public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";
    public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String BEAN_ELEMENT = "bean";
    public static final String META_ELEMENT = "meta";
    public static final String ID_ATTRIBUTE = "id";
    public static final String PARENT_ATTRIBUTE = "parent";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String ABSTRACT_ATTRIBUTE = "abstract";
    public static final String SCOPE_ATTRIBUTE = "scope";
    private static final String SINGLETON_ATTRIBUTE = "singleton";
    public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
    public static final String AUTOWIRE_ATTRIBUTE = "autowire";
    public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";
    public static final String PRIMARY_ATTRIBUTE = "primary";
    public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";
    public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";
    public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";
    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    //...

    protected final Log logger = LogFactory.getLog(getClass());
    private final XmlReaderContext readerContext;
    private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();

    private final ParseState parseState = new ParseState();

    private final Set<String> usedNames = new HashSet();

    public BeanDefinitionParserDelegate(XmlReaderContext readerContext)
    {
        this.readerContext = readerContext;
    }

    public final XmlReaderContext getReaderContext()
    {
        return this.readerContext;
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri)) ||
                (BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isDefaultNamespace(Node node) {
        //return isDefaultNamespace(getNamespaceURI(node));
        return false;
    }

    public void initDefaults(Element root)
    {
        initDefaults(root, null);
    }

    public void initDefaults(Element root, BeanDefinitionParserDelegate parent)
    {
       //...
    }

    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele)
    {
        return parseBeanDefinitionElement(ele, null);
    }

    //对于<bean id="aa" name="aaa" class="com.xxx.Aa"></bean>这个bean的解析
    //在parseBeanDefinitionElement中会完成id和name标签的解析处理操作，并最终生成BeanDefinitionHolder返回
    //class标签的元素解析在parseBeanDefinitionElement中处理
    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean)
    {
        //解析id
        String id = ele.getAttribute(ID_ATTRIBUTE);
        //解析name
        String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

        //获取别名
        List<String> aliases = new ArrayList<String>();
        if (StringUtils.hasLength(nameAttr)) {
            String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, ",; ");
            aliases.addAll(Arrays.asList(nameArr));
        }

        String beanName = id;
        if ((!StringUtils.hasText(beanName)) && (!aliases.isEmpty())) {
            beanName = (String)aliases.remove(0);
        }
        //...
        //解析其他元素标签
        AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
        if (beanDefinition != null) {
            if (!StringUtils.hasText(beanName)) {
               //...
            }
            String[] aliasesArray = StringUtils.toStringArray(aliases);
            //最终生成BeanDefinitionHolder
            //return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
            return new BeanDefinitionHolder(null,beanName);
        }
        return null;
    }

    //parseBeanDefinitionElement中会完成对class等其他标签的解析处理操作，最终生成BeanDefinition返回
    public AbstractBeanDefinition parseBeanDefinitionElement(Element ele, String beanName, BeanDefinition containingBean)
    {
        this.parseState.push(new BeanEntry(beanName));

        String className = null;
        //解析class标签内容
        if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
            className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
        }
        try
        {
            String parent = null;
            if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
                parent = ele.getAttribute(PARENT_ATTRIBUTE);
            }
            //AbstractBeanDefinition bd = createBeanDefinition(className, parent);
            AbstractBeanDefinition bd=null;

            //parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
            bd.setDescription(DomUtils.getChildElementValueByTagName(ele, "description"));
            //...
            AbstractBeanDefinition localAbstractBeanDefinition1 = bd;
            return localAbstractBeanDefinition1;
        }catch (Throwable ex) {
            //error("Unexpected failure during bean definition parsing", ele, ex);
        }
        finally {
            this.parseState.pop();
        }
        return null;
    }

    public String getLocalName(Node node)
    {
        return node.getLocalName();
    }

    public boolean nodeNameEquals(Node node, String desiredName)
    {
        return (desiredName.equals(node.getNodeName())) || (desiredName.equals(getLocalName(node)));
    }

    //对命名空间下的标签进行处理
    public BeanDefinition parseCustomElement(Element ele) {
        return parseCustomElement(ele, null);
    }

    public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
        //获取xml配置文件中的命名空间,比如http://www.springframework.org/schema/aop
        String namespaceUri = getNamespaceURI(ele);
        //根据命名空间找到命名空间处理类,比如AopNamespaceHandler
        NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
        if (handler == null) {
            return null;
        }
        //解析命名空间支持的标签
        return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
    }

    public BeanDefinitionHolder decorateBeanDefinitionIfRequired(Element ele, BeanDefinitionHolder definitionHolder) {
        //return decorateBeanDefinitionIfRequired(ele, definitionHolder, null);
        return null;
    }

    public String getNamespaceURI(Node node)
    {
        return node.getNamespaceURI();
    }

}
