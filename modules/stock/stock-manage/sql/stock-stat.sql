
-- 查询当前股价不超过历史年低价20%的股票
-- 历史年低价需要限定启始年限，一般在2020年以后，
-- 因为2020年股票进行了注册制股改，在这一年很多股票度大涨，之前的价格都远低于当前股价
select cur.stock_code, cur.stock_name, cur.end_price, b.min_price  
from t_stock_year cur 
join (
   select c.stock_code,min(c.min_price ) min_price 
	 from t_stock_year c 
	 where c.stat_year >2020 and c.stat_year<2025
	 group by c.stock_code 	
) b on cur.stock_code =b.stock_code 
where cur.stat_year =2025
and (cur.end_price <= b.min_price*1.2 ) 
 