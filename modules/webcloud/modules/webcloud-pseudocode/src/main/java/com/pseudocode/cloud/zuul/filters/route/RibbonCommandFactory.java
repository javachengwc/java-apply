package com.pseudocode.cloud.zuul.filters.route;

import com.pseudocode.cloud.ribbon.support.RibbonCommandContext;

public interface RibbonCommandFactory<T extends RibbonCommand> {

    T create(RibbonCommandContext context);

}
