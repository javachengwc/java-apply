package com.manage.model.main;

/**
 * 所有的物质
 */
public class MaterialExt {

	public static final String IDSPIDT="_";
	
	public static enum  MaType{
		Card(0,"卡牌"), Material(1,"道具"),Equip(2,"装备");
		
		private final int value;
		
		private final String name;
		
		private MaType(final int value,final String name)
		{
			this.value=value;
			this.name=name;
		}
		
		public int getValue()
		{
			return value;
		}
		public String getName()
		{
			return name;
		}
		
	}
	private int id;
	/**
	 * 	组合id	
	 */
	private String combinid;
	
	private String name;
	
	private String icon;
	 
	private String desc;
	
	private int type;

	private MaType maType;

	/**物质类型 
	 * 0--卡牌  1--道具 2--装备
	 */
	private Integer matelType;
	
	public MaterialExt()
	{
		
	}
	public MaterialExt(int id,int materType)
	{
		this.id=id;
		this.setMatelType(materType);
		this.combinid=maType.getValue()+IDSPIDT+id;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCombinid() {
		if(combinid!=null && !"".equals(combinid) && !"".equals(combinid.trim()))
		    return combinid;
		else
			return maType.getValue()+IDSPIDT+id;
	}  

	public void setCombinid(String combinid) {
		this.combinid = combinid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public MaType getMaType() {
		return maType;
	}
	
	public void setMaType(MaType maType) {
		this.maType = maType;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Integer getMatelType() {
		return matelType;
	}

	public void setMatelType(Integer matelType) {
		
		this.matelType = matelType;
		
		for(MaType t:MaType.values())
		{
			if(matelType==t.getValue())
			{
		       this.maType=t;
		       break;
			}
		}
	}

	public int getMaTypeInt()
	{
		return this.maType.getValue();
	}
	public String getMaTypeStr()
	{
		return this.maType.getName();
	}
	
	
}
