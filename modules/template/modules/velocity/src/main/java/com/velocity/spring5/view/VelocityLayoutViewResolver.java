package com.velocity.spring5.view;


import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class VelocityLayoutViewResolver extends VelocityViewResolver {

    private String layoutUrl;

    private String layoutKey;

    private String screenContentKey;

    @Override
    protected Class<?> requiredViewClass() {
        return VelocityLayoutView.class;
    }

    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    public void setLayoutKey(String layoutKey) {
        this.layoutKey = layoutKey;
    }

    public void setScreenContentKey(String screenContentKey) {
        this.screenContentKey = screenContentKey;
    }


    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        VelocityLayoutView view = (VelocityLayoutView) super.buildView(viewName);
        // Use not-null checks to preserve VelocityLayoutView's defaults.
        if (this.layoutUrl != null) {
            view.setLayoutUrl(this.layoutUrl);
        }
        if (this.layoutKey != null) {
            view.setLayoutKey(this.layoutKey);
        }
        if (this.screenContentKey != null) {
            view.setScreenContentKey(this.screenContentKey);
        }
        return view;
    }

}
