package com.manageplat.model.vo;

public  enum ConditionEnum{

    DAYS("a","访问天数","days"),OUT("b","out数","out_count"),ORDER_COUNT("c","下单数","order_count"),ORDER_AMOUNT("d","下单金额","order_amount"),
    ORDER_AVG_AMOUNT("e","下单单价","order_avg_amount"),SIGN("f","签到天数","sign_days"),LOGIN("g","登录天数","login_days");

    private ConditionEnum(String flag,String title,String col)
    {

        this.flag=flag;
        this.title =title;
        this.col=col;
    }

    private String flag;
    private String title;
    private String col;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public static String getAllSelectValue()
    {
        StringBuffer buf =new StringBuffer();
        for(ConditionEnum per:ConditionEnum.values())
        {
            buf.append(per.getFlag());
        }
        return buf.toString();
    }

    public static ConditionEnum findByFlag(String flag)
    {
        for(ConditionEnum per:ConditionEnum.values())
        {
            if(per.getFlag().equals(flag))
            {
                return per;
            }
        }
        return null;
    }
}
