package com.mountain.util;

import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class ServiceUrlUtil {

    public static boolean isMatchCategory(String category, String consumerCategory) {
        if (StringUtils.isBlank(consumerCategory)) {
            return Constant.DEFAULT_CATEGORY.equals(category);
        } else if (consumerCategory.contains(Constant.ANY_VALUE)) {
            return true;
        } else if (consumerCategory.contains(Constant.VALUE_PREFIX)) {
            return !consumerCategory.contains(Constant.VALUE_PREFIX + category);
        } else {
            return consumerCategory.contains(category);
        }
    }

    public static boolean isMatch(SpecUrl consumerUrl, SpecUrl providerUrl) {
        String consumerInterface = consumerUrl.getInterface();
        String providerInterface = providerUrl.getInterface();
        if( ! (Constant.ANY_VALUE.equals(consumerInterface) || consumerInterface.equals(providerInterface)) )
        {
            return false;
        }

        String consumerCategory=consumerUrl.getParameter(Constant.CATEGORY_KEY, Constant.DEFAULT_CATEGORY);
        String providerCategory=providerUrl.getParameter(Constant.CATEGORY_KEY, Constant.DEFAULT_CATEGORY);
        if (! isMatchCategory(providerCategory,consumerCategory))
        {
            return false;
        }

        boolean providerEnable= providerUrl.getParameter(Constant.ENABLED_KEY, true);

        if (!providerEnable && !Constant.ANY_VALUE.equals(consumerUrl.getParameter(Constant.ENABLED_KEY)))
        {
            return false;
        }
        String consumerGroup = consumerUrl.getParameter(Constant.GROUP_KEY, Constant.ANY_VALUE);
        String consumerVersion = consumerUrl.getParameter(Constant.VERSION_KEY);
        String consumerClassifier = consumerUrl.getParameter(Constant.CLASSIFIER_KEY, Constant.ANY_VALUE);

        String providerGroup = providerUrl.getParameter(Constant.GROUP_KEY, Constant.ANY_VALUE);
        String providerVersion = providerUrl.getParameter(Constant.VERSION_KEY);
        String providerClassifier = providerUrl.getParameter(Constant.CLASSIFIER_KEY, Constant.ANY_VALUE);

        boolean groupMatch =(consumerGroup==null || Constant.ANY_VALUE.equals(consumerGroup) || consumerGroup.equals(providerGroup) || Arrays.asList(consumerGroup.split(",")).contains(providerGroup));
        if(!groupMatch)
        {
            return false;
        }
        boolean versionMatch =  (Constant.ANY_VALUE.equals(consumerVersion) || consumerVersion.equals(providerVersion));
        if(!versionMatch)
        {
            return false;
        }
        boolean classifierMatch= (consumerClassifier == null || Constant.ANY_VALUE.equals(consumerClassifier) || consumerClassifier.equals(providerClassifier));
        return classifierMatch;
    }

}
